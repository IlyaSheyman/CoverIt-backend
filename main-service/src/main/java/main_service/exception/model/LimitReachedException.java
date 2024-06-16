package main_service.exception.model;

import lombok.Getter;
import main_service.exception.dto.LimitExceptionMessage;

@Getter
public class LimitReachedException extends RuntimeException {
    public LimitExceptionMessage response;

    public LimitReachedException(LimitExceptionMessage response) {
        this.response = response;
    }
}
