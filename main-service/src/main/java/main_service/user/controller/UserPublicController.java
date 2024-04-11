package main_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "Public controller. For setting up subscription")
public class UserPublicController {

    private final UserService service;
    @Operation(summary = "verify subscription")
    @GetMapping("/verify/subscription")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void verifySubscription(@RequestParam(value = "code") String code) {
        log.info("[USER_CONTROLLER] subscribe user");

        service.verifySubscription(code);
    }
}
