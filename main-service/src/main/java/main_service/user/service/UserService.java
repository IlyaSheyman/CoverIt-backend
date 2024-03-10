package main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.dto.UserUpdatePasswordDto;
import main_service.user.mapper.UserMapper;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    @Qualifier("userMapperImpl")
    private final UserMapper mapper;

    // Старая реализация логин
//    @Override
//    public UserLoginDto login(UserLoginDto dto) {
//        String email = dto.getEmail();
//        if (!repository.existsByEmailIgnoreCase(email)) {
//            throw new BadRequestException(String.format("User with email %s doesn't exist", email));
//        }
//
//        User user = repository.findByEmail(email);
//
//        if (!user.getPassword().equals(dto.getPassword())) {
//            throw new BadRequestException("Incorrect password");
//        }
//
//        if (user.getIsAuthenticated() == true) {
//            throw new ConflictRequestException(String.format("User %s is already authenticated", user.getUsername()));
//        } else {
//            user.setIsAuthenticated(true);
//        }
//
//        return mapper.toUserLoginDto(repository.save(user));
//    }

    private String isExistsUsername(String username) {
        if (repository.existsByUsernameIgnoreCase(username)) {
            throw new ConflictRequestException(String.format("User with username %s already exists", username));
        } else {
            return username;
        }
    }

    private String isExistsEmail(String email) {
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ConflictRequestException(String.format("User with email %s already exists", email));
        } else {
            return email;
        }
    }

    private User isExistsById(int userId) {
        return repository
                .findById(userId)
                .orElseThrow(()-> new NotFoundException(String.format("User with id %d not found", userId)));
    }

    public void delete(int userId) {
        isExistsById(userId);
        repository.deleteById(userId);
        log.info("[USER_SERVICE] user with id {} is deleted successfully", userId);
    }

    public UserUpdateDto updateUsername(int userId, UserUpdateDto dto) {

        User user = isExistsById(userId);
        user.setUsername(dto.getUsername());
        repository.save(user);

        return dto;
    }

    public UserLoginDto updatePassword(int userId, UserUpdatePasswordDto dto) {
        User user = isExistsById(userId);

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password confirmation exception");
        } else {
            user.setPassword(dto.getPassword());
        }

        repository.save(user);

        return mapper.toUserLoginDto(user);
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
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        User user = repository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException("User with username " + username + " not found");
        }
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}