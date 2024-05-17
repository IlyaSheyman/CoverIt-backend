package main_service.logs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static main_service.constants.Constants.DATE_FORMAT;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class LogMessage {
    private final String header;
    private final String message;
    @JsonFormat(pattern = DATE_FORMAT)
    private final LocalDateTime timestamp;
    private final Object jsonBody;
    private final String coverUrl;
}
