package main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.dto.UserUpdateDto;
import main_service.user.mapper.UserMapper;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    @Qualifier("userMapperImpl")
    private final UserMapper mapper;
    private final JwtService jwtService;

    public void updateUsername(String userToken, UserUpdateDto dto) {
        User user = getUserById(jwtService.extractUserId(userToken));

        String newUsername = dto.getUsername();
        
        if (repository.getByUsername(newUsername) == null) {
            user.setUsername(newUsername);
            repository.save(user);
            log.info("[USERSERVICE] Username of user with id " + user.getId() + " updated successfully. New username: " + user.getUsername());
        } else {
            throw new ConflictRequestException(String.format("User with username %s already exists", newUsername));
        }
    }

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsernameIgnoreCase(user.getUsername())) {
            throw new BadRequestException("Пользователь с таким именем уже существует");
        }
        if (repository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new BadRequestException("Пользователь с таким email уже существует");
        }
        return save(user);
    }

    /**
     * Getting user by username
     *
     * @return user
     */
    public User getByUsername(String username) {
        User user = repository.getByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException("User with username " + username + " not found");
        }
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
                .orElseThrow(()-> new NotFoundException(String.format("User with id %d not found", userId)));
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return getByEmail(username);
            }
        };

        return userDetailsService;
    }
}