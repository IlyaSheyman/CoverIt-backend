package main_service.user.service;

import main_service.user.dto.UserCreateDto;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;

public interface UserService {
    UserCreateDto create(UserCreateDto dto);

    UserLoginDto login(UserLoginDto dto);
}
