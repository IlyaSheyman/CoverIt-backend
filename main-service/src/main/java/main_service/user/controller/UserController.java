package main_service.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.user.dto.UserCreateDto;
import main_service.user.dto.UserLoginDto;
import main_service.user.dto.UserUpdateDto;
import main_service.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequestMapping(name = "/user")
@RequiredArgsConstructor
public class UserController {

    @Qualifier("UserServiceImpl")
    private final UserService service;

    @PostMapping(name = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateDto create(@RequestBody @Valid UserCreateDto dto) {
        log.info("[USER_CONTROLLER] create user {}", dto.toString());

        return service.create(dto);
    }

    @PostMapping(name = "/login")
    public UserLoginDto login(@RequestBody @Valid UserLoginDto dto) {
        log.info("[USER_CONTROLLER] login user {}", dto.toString());

        return service.login(dto);
    }

    @PatchMapping
    public void update() {

    }

    @DeleteMapping
    public void delete() {

    }
}