package main_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.user.dto.UserCreateDto;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.dto.UserUpdatePasswordDto;
import main_service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Qualifier("UserServiceImpl")
    private final UserService service;

    @Operation(
            description = "Register user with email and password + password confirmation",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    schema = @Schema(implementation = UserCreateDto.class)
                            )
                    )
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateDto create(@RequestBody @Valid UserCreateDto dto) {
        log.info("[USER_CONTROLLER] create user {}", dto.toString());

        return service.create(dto);
    }

    @Operation(
            description = "Login user with email and password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = UserLoginDto.class)
                            )
                    )
            }
    )
    @PostMapping("/login")
    public UserLoginDto login(@RequestBody @Valid UserLoginDto dto) {
        log.info("[USER_CONTROLLER] login user {}", dto.toString());

        return service.login(dto);
    }

    @Operation(
            description = "Update username by userId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = UserUpdateDto.class)
                            )
                    )
            }
    )
    @PatchMapping("/update/username")
    public UserUpdateDto updateUsername(@RequestHeader(name = "X-User-Id") int userId,
                       @RequestBody @Valid UserUpdateDto dto) {
        log.info("[USER_CONTROLLER] update user's with id {} username", userId);

        return service.updateUsername(userId, dto);
    }

    @Operation(
            description = "Update password by userId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = UserLoginDto.class)
                            )
                    )
            }
    )
    @PatchMapping("/update/password")
    public UserLoginDto updateUsername(@RequestHeader(name = "X-User-Id") int userId,
                                                @RequestBody @Valid UserUpdatePasswordDto dto) {
        log.info("[USER_CONTROLLER] update user's with id {} password", userId);

        return service.updatePassword(userId, dto);
    }

    @Operation(
            description = "Delete username by userId",
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    )
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void delete(@RequestHeader(name = "X-User-Id") int userId) {
        log.info("[USER_CONTROLLER] delete user with id {}", userId);

        service.delete(userId);
    }
}
