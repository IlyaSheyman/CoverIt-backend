package main_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtAuthenticationResponse;
import main_service.user.dto.SignInRequest;
import main_service.user.dto.SignUpRequest;
import main_service.user.service.AuthenticationService;
import main_service.user.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Public controller")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "User's registration")
    @PostMapping("/sign-up")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignUpRequest userDto, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        log.info("[AUTH_CONTROLLER] sign-up user with username " + userDto.getUsername());
        authenticationService.signUp(userDto, getSiteURL(request));
    }
    @Operation(summary = "User's email verification. Used as a link sent to email")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/verify")
    public void verifyUser(@Param("code") String code) {
        userService.verify(code);
    }

    @Operation(summary = "User's authorization")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        log.info("[AUTH_CONTROLLER] sign-in user with email " + request.getEmail());
        return authenticationService.signIn(request);
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
