package main_service.user.service;

import main_service.user.dto.UserCreateDto;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.dto.UserUpdatePasswordDto;

public interface UserService {
    UserCreateDto create(UserCreateDto dto);

    UserLoginDto login(UserLoginDto dto);

    void delete(int userId);

    UserUpdateDto updateUsername(int userId, UserUpdateDto dto);

    UserLoginDto updatePassword(int userId, UserUpdatePasswordDto dto);
}
