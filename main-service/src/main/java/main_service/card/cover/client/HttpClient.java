package main_service.card.cover.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main_service.card.cover.server.service.UrlDto;
import main_service.constants.Constants;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class HttpClient {
    protected final RestTemplate rest;

    public HttpClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity cover(UrlDto body, Constants.Vibe vibe, Boolean isAbstract) {
        Map<String, Object> params = Map.of("vibe", vibe,
                "is_abstract", isAbstract);

        return makeAndSendRequest(
                POST,
                "/cover",
                params,
                body);
    }

    public ResponseEntity<Object> getPlaylistDto(String url) {
        UrlDto urlDto = UrlDto.builder()
                .link(url)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String urlDtoJson;

        try {
            urlDtoJson = objectMapper.writeValueAsString(urlDto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return makeAndSendRequest(
                GET,
                "/playlist",
                null,
                urlDtoJson
        );
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(
            HttpMethod method,
            String path,
            @Nullable Map<String, Object> parameters,
            @Nullable T body) {

        assert body != null;
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<Object> serverResponse;

        try {
            if (parameters != null) {
                serverResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        Object.class,
                        parameters);
            } else {
                serverResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}