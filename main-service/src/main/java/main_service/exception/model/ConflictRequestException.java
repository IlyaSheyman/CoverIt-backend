package main_service.exception.model;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }
}
