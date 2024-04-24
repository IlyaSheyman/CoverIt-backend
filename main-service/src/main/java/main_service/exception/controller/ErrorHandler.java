package main_service.exception.controller;

import lombok.extern.slf4j.Slf4j;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.ErrorResponse;
import main_service.exception.model.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static main_service.constants.Constants.DATE_FORMAT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ErrorResponse("Not found",
                e.getMessage(),
                e.getCause().getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(formatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectRequest(final BadRequestException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse("Bad request",
                e.getMessage(),
                e.getCause().getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse("Argument not valid",
                e.getMessage(),
                e.getCause().getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse("Illegal argument",
                e.getMessage(),
                e.getCause().getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectRequest(final ConflictRequestException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse("Conflict",
                e.getMessage(),
                e.getCause().getMessage(),
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.warn("500 {}", e.getMessage(), e);
        return new ErrorResponse("Internal server error",
                e.getMessage(),
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().format(formatter));
    }
}