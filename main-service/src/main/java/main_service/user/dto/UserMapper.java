package main_service.user.dto;

import main_service.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "string")
public interface UserMapper {

    UserCreateDto toUserCreateDto(User user);

    UserLoginDto toUserLoginDto(User user);

}
