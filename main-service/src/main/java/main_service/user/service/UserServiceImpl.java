package main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.dto.UserCreateDto;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.dto.UserUpdatePasswordDto;
import main_service.user.mapper.UserMapper;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserCreateDto create(UserCreateDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password confirmation exception");
        }

        String email = isExistsEmail(dto.getEmail());
        String username = isExistsUsername(dto.getUsername());

        User user = User.builder()
                .password(dto.getPassword())
                .email(email)
                .username(username)
                .isAuthenticated(true)
                .build();

        return mapper.toUserCreateDto(repository.save(user));
    }

    @Override
    public UserLoginDto login(UserLoginDto dto) {
        String email = dto.getEmail();
        if (!repository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException(String.format("User with email %s doesn't exist", email));
        }

        User user = repository.findByEmail(email);

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }

        if (user.getIsAuthenticated() == true) {
            throw new ConflictRequestException(String.format("User %s is already authenticated", user.getUsername()));
        } else {
            user.setIsAuthenticated(true);
        }

        return mapper.toUserLoginDto(repository.save(user));
    }

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

    @Override
    public void delete(int userId) {
        isExistsById(userId);
        repository.deleteById(userId);
        log.info("[USER_SERVICE] user with id {} is deleted successfully", userId);
    }

    @Override
    public UserUpdateDto updateUsername(int userId, UserUpdateDto dto) {

        User user = isExistsById(userId);
        user.setUsername(dto.getUsername());
        repository.save(user);

        return dto;
    }

    @Override
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
}