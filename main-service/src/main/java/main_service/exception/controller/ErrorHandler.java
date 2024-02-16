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
        return new ErrorResponse(e.getMessage(),
                "Not found exception",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(formatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectRequest(final BadRequestException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(),
                "Bad request exception",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(),
                "Method argument not valid",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(),
                "Illegal argument exception",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectRequest(final ConflictRequestException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(),
                "Conflict request exception",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.warn("500 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(),
                "Undefined exception",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().format(formatter));
    }
}