package main_service.user.service;

import main_service.user.dto.UserCreateDto;
import main_service.user.dto.UserLoginDto;

public interface UserService {
    UserCreateDto create(UserCreateDto dto);

    UserLoginDto login(UserLoginDto dto);
}
