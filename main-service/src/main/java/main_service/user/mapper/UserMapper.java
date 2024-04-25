package main_service.user.mapper;

import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserProfileDto;
import main_service.user.dto.UserSmallDto;
import main_service.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserLoginDto toUserLoginDto(User user);
    UserProfileDto toUserProfileDto(User user);
    UserSmallDto toUserSmallDto(User user);
}