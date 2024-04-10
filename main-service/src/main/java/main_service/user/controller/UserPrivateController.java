package main_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.user.dto.UserSmallDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.service.UserService;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "search users in 'find users'")
    @GetMapping("/find_users")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<UserSmallDto> searchUsers(@RequestHeader(name = "Authorization") String userToken,
                                          @RequestParam(name = "search") String search,
                                          @RequestBody @Valid UserUpdateDto dto,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("[USER_CONTROLLER] update username for user");

        if (userToken != null) {
            userToken = userToken.substring(7);
        }

        service.search(userToken, search, dto, page, size);
        return null;
    }


}