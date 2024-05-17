package main_service.logs.client;

import lombok.extern.slf4j.Slf4j;
import main_service.logs.model.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TelegramLogsClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    private final String token;

    @Autowired
    public TelegramLogsClient(RestTemplateBuilder builder,
                              @Value("${logs.server.url}") String serverUrl,
                              @Value("${logs.server.token}") String token) {
        this.restTemplate = builder.build();
        this.serverUrl = serverUrl;
        this.token = token;
    }

    public void sendLogMessage(LogMessage message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("secret-token-shit", token);
        HttpEntity<LogMessage> request = new HttpEntity<>(message, headers);

        try {
            restTemplate.postForLocation(serverUrl + "/logs", request);
            log.info("Log message sent successfully.");
        } catch (Exception e) {
            log.error("Failed to send log message: {}", e.getMessage());
        }
    }
}