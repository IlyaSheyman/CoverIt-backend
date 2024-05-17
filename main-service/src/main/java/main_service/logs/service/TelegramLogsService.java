package main_service.logs.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.logs.client.TelegramLogsClient;
import main_service.logs.model.LogMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramLogsService {
    private final TelegramLogsClient client;

    public void info(String header, String message, Object object, String link) {

        LogMessage logMessage = LogMessage.builder()
                .header(header)
                .message(message)
                .jsonBody(object)
                .timestamp(LocalDateTime.now())
                .coverUrl(link)
                .build();

        client.sendLogMessage(logMessage);
        log.info(message);
    }
}