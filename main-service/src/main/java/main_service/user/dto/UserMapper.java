package main_service.user.dto;

import main_service.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "confirmPassword", source = "user.password")
    UserCreateDto toUserCreateDto(User user);

    UserLoginDto toUserLoginDto(User user);

}