package main_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.dto.UserUpdatePasswordDto;
import main_service.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "Private controller. For changing user's properties")
public class UserPrivateController {

    private final UserService service;

    @Operation(summary = "Update username")
    @PatchMapping("/update/username")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUsername(@RequestHeader(name = "Authorization") String userToken,
                       @RequestBody @Valid UserUpdateDto dto) {
        log.info("[USER_CONTROLLER] update username for user");
        userToken = userToken.substring(7);
        service.updateUsername(userToken, dto);
    }

}