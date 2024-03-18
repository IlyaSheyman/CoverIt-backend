package main_service.card.cover.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main_service.card.cover.server.service.UrlDto;
import main_service.card.playlist.dto.PlaylistDto;
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

    protected ResponseEntity<String> cover(UrlDto body, Constants.Vibe vibe, Boolean isAbstract, String response) {
        Map<String, Object> params = Map.of("is_abstract", isAbstract);

        if (vibe != null) {
            params.put("vibe", vibe);
        }

        return makeAndSendRequest(
                POST,
                "/cover",
                params,
                body,
                response
        );
    }

    public ResponseEntity<PlaylistDto> getPlaylistDto(String url, PlaylistDto response) {
        UrlDto urlDto = UrlDto.builder()
                .link(url)
                .build();

//        ObjectMapper objectMapper = new ObjectMapper();
//        String urlDtoJson;
//
//        try {
//            urlDtoJson = objectMapper.writeValueAsString(urlDto);
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }

        return makeAndSendRequest(
                GET,
                "/playlist",
                null,
                urlDto,
                response
        );
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

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}