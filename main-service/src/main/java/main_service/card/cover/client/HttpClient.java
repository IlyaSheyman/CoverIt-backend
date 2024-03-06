package main_service.card.cover.client;

import main_service.card.cover.server.service.UrlDto;
import main_service.constants.Constants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.POST;

public class HttpClient {
    protected final RestTemplate rest;

    public HttpClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity cover(UrlDto body, Constants.Vibe vibe, Boolean isAbstract, String response) {
        Map<String, Object> params = Map.of("vibe", vibe,
                "is_abstract", isAbstract);

        return makeAndSendRequest(
                POST,
                "/cover",
                params,
                body,
                response);
    }

    private <T, K> ResponseEntity<K> makeAndSendRequest(
            HttpMethod method,
            String path,
            @Nullable Map<String, Object> parameters,
            @Nullable T body,
            K response) {

        assert body != null;
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        Class kClass = response.getClass();
        ResponseEntity<K> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        kClass,
                        parameters);
            } else {
                serverResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        kClass);
            }
        } catch (HttpStatusCodeException e) {
            return (ResponseEntity<K>) ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
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