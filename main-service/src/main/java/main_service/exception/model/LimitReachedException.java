package main_service.exception.model;

public class LimitReachedException extends RuntimeException {
    public LimitReachedException(String message) {
        super(message);
    }
}
