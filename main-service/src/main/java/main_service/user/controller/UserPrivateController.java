package main_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.user.dto.UserProfileDto;
import main_service.user.dto.UserSmallDto;
import main_service.user.dto.UserSubscriptionDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        service.updateUsername(userToken, dto);
    }

    @Operation(summary = "search users in 'find users'")
    @GetMapping("/find")
    public List<UserSmallDto> searchUsers(@RequestHeader(name = "Authorization") String userToken,
                                          @RequestParam(name = "search") String search,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("[USER_CONTROLLER] search users containing " + search + " in their username");

        return service.search(search, page, size);
    }

    @Operation(summary = "get current user's profile info")
    @GetMapping("/me")
    public UserProfileDto getCurrentUserProfile(@RequestHeader(name = "Authorization") String userToken) {
        log.info("request to get current user's profile");

        return service.getCurrentUserProfile(userToken);
    }

    @Operation(summary = "get current user's subscription info")
    @GetMapping("/subscription")
    public UserSubscriptionDto getUserSubscription(@RequestHeader(name = "Authorization") String userToken) {
        log.info("request to get current user's subscription info");

        return service.getUserSubscription(userToken);
    }

}