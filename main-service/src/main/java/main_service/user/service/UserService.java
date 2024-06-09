package main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
import main_service.logs.service.TelegramLogsService;
import main_service.user.client.PatreonClient;
import main_service.user.dto.*;
import main_service.user.entity.User;
import main_service.user.mapper.UserMapper;
import main_service.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main_service.constants.Constants.SUBSCRIPTION_GENERATIONS_LIMIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    @Qualifier("userMapperImpl")
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final PatreonClient patreonClient;
    private final TelegramLogsService logsService;

    public void updateUsername(String userToken, UserUpdateDto dto) {
        User user = extractUserFromToken(userToken);

        String newUsername = dto.getUsername();

        if (repository.getByUsername(newUsername) == null) {
            user.setUsername(newUsername);
            user = repository.save(user);

            logsService.info("User updated username",
                    String.format("User with id %d has updated username. New username: <b>%s</b>", user.getId(),
                            user.getUsername()),
                    mapper.toUserLogsDto(user),
                    null);
        } else {
            throw new ConflictRequestException(String.format("User with username %s already exists", newUsername));
        }
    }

    /**
     * Saving user
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Creating user
     *
     * @return created user
     */
    public User create(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        if (repository.existsByUsernameIgnoreCaseAndEnabled(username, true)) {
            throw new BadRequestException("User with this username already exists");
        }
        if (repository.existsByEmailIgnoreCaseAndEnabled(email, true)) {
            throw new BadRequestException("User with this email already exists");
        }
        return save(user);
    }

    /**
     * Getting user by email
     *
     * @return user
     */
    public User getByEmail(String email) {
        User user = repository.getByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException("User with email " + email + " not found");
        }
    }

    /**
     * Getting user by id
     *
     * @return user
     */
    public User getUserById(int userId) {
        return repository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return getByEmail(username);
            }
        };
    }

    public List<UserSmallDto> search(String search, int page, int size) {
        return repository.findByUsernameContaining(search, PageRequest.of(page, size))
                .stream()
                .map(mapper::toUserSmallDto)
                .toList();
    }

    public void verify(String verificationCode) {
        User user = repository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            throw new ConflictRequestException("Verification fail");
        } else {
            user.setEnabledAt(LocalDateTime.now());
            user.setVerificationCode(null);
            user.setEnabled(true);

            repository.save(user);

            log.info("User with id " + user.getId() + " is verified");
            logsService.info("New user",
                    String.format("User with id %d is created and verified", user.getId()),
                    mapper.toUserLogsDto(user),
                    null);
        }
    }

    public UserProfileDto getCurrentUserProfile(String userToken) {
        User current = extractUserFromToken(userToken);
        return mapper.toUserProfileDto(current);
    }

    public UserSubscriptionDto getUserSubscription(String userToken) {
        User current = extractUserFromToken(userToken);
        if (current.isSubscribed()) {
            LocalDateTime updateAt = LocalDateTime.of(LocalDateTime.now().getYear(),
                    LocalDateTime.now().getMonthValue(),
                    current.getSubscribedAt().getDayOfMonth(),
                    LocalDateTime.now().getHour(),
                    LocalDateTime.now().getMinute(),
                    LocalDateTime.now().getSecond());

            int generationsLeft = SUBSCRIPTION_GENERATIONS_LIMIT -
                    (current.getHiFiPlaylistGenerations()
                            + current.getLoFiPlaylistGenerations()
                            + current.getLoFiReleaseGenerations()
                            + current.getHiFiReleaseGenerations());

            return UserSubscriptionDto.builder()
                    .subscribed(current.isSubscribed())
                    .generationsUpdateAt(updateAt)
                    .generationsLeft(generationsLeft)
                    .email(current.getEmail())
                    .build();
        } else {
            return UserSubscriptionDto.builder()
                    .email(current.getEmail())
                    .build();
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    @Async
    public void updateGenerationsCountForAllUsers() {
        List<User> users = repository.findAll();
        int counter = 0;

        for (User user : users) {
            if (!user.isSubscribed()) {
                user.setLoFiReleaseGenerations(0);
                user.setHiFiReleaseGenerations(0);
                counter++;
                repository.save(user);
            } else {
                if (LocalDateTime.now().getDayOfMonth() == user.getSubscribedAt().getDayOfMonth()) {
                    renewCountersForSubscribed(user);
                    counter++;
                }
            }
        }
        logsService.info("Generations updated",
                String.format("Generations counters were renewed for %d users", counter),
                null,
                null);
    }

    @Scheduled(cron = "0 0 23 * * *")
    @Transactional
    @Async
    public void verifySubscribersList() {
        log.info("starting to verify subscribers list");

        List<String> dbPatronsNames = repository.findBySubscribedTrue().stream()
                .map(User::getPatronName)
                .filter(Objects::nonNull)
                .toList();

        List<String> patreonPatronsNames = patreonClient.getPatronsNames();

        List<String> missingPatronNames = new ArrayList<>(dbPatronsNames);
        missingPatronNames.removeAll(patreonPatronsNames);

        if (!missingPatronNames.isEmpty()) {
            for (String name : missingPatronNames) {
                User unsubscribed = repository.findByPatronName(name);

                unsubscribed.setSubscribed(false);
                unsubscribed.setSubscribedAt(null);
                unsubscribed.setPatronName(null);

                repository.save(unsubscribed);
                logsService.info("Subscriber removed",
                        String.format("User <b>@%s</b> is not subscriber any more :(", unsubscribed.getUsername()),
                        null,
                        null);
            }
        }

        logsService.info("Subscribers list is verified",
                String.format("Current number of subscribers: <b>%d</b>", patreonPatronsNames.size()),
                patreonPatronsNames,
                null);
    }

    public void verifySubscription(String code) {
        String accessToken = patreonClient.getAccessToken(code);
        PatronDto patron = patreonClient.getPatron(accessToken);
        log.info("patron: " + patron.toString());

        String email = patron.getEmail();
        String patronName = patron.getFullName();

        if (patreonClient.getPatronsNames().contains(patronName)) {
            User user = getByEmail(email);

            if (!user.isSubscribed()) {
                renewCountersForSubscribed(user);
                user.setSubscribed(true);
                user.setSubscribedAt(LocalDateTime.now());
                user.setPatronName(patronName);

                repository.save(user);

                log.info("user with email " + email + " is subscribed now");
            } else {
                throw new ConflictRequestException("You are already subscribed");
            }
        } else {
            throw new NotFoundException("You are not present in subscribers list");
        }
    }

    private void renewCountersForSubscribed(User user) {
        user.setHiFiReleaseGenerations(0);
        user.setLoFiReleaseGenerations(0);
        user.setLoFiPlaylistGenerations(0);
        user.setHiFiPlaylistGenerations(0);
    }

    private User extractUserFromToken(String userToken) {
        userToken = userToken.substring(7);
        return getUserById(jwtService.extractUserId(userToken));
    }
}