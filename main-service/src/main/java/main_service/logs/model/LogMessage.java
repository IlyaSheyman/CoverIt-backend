package main_service.logs.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class LogMessage {
    private final String header;
    private final String message;
    private final String timestamp;
    private final String stackTrace;
}
