package main_service.cover.client;

import main_service.constants.Constants;
import main_service.cover.service.UrlDto;
import main_service.logs.model.LogMessage;
import main_service.playlist.dto.CoverResponse;
import main_service.playlist.dto.PlaylistDto;
import main_service.release.dto.ReleaseRequestDto;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

public class HttpClient {
    protected final RestTemplate rest;

    public HttpClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<CoverResponse> coverRelease(ReleaseRequestDto body, CoverResponse response) {

        Map<String, Object> params = new HashMap<>();

        return makeAndSendRequest(
                POST,
                "/cover_release",
                params,
                body,
                response
        );
    }

    protected ResponseEntity<CoverResponse> coverPlaylist(UrlDto body,
                                                          Constants.Vibe vibe,
                                                          Boolean isAbstract,
                                                          Boolean isLoFi,
                                                          CoverResponse response) {

        Map<String, Object> params = new HashMap<>(Map.of("is_abstract", isAbstract, "is_lofi", isLoFi));

        if (vibe != null) {
            params.put("vibe", vibe);
        } else {
            params.put("vibe", "none");
        }

        return makeAndSendRequest(
                POST,
                "/cover_playlist?vibe={vibe}&is_abstract={is_abstract}&is_lofi={is_lofi}",
                params,
                body,
                response
        );
    }

    public ResponseEntity<PlaylistDto> getPlaylistDto(String url, PlaylistDto response) {
        UrlDto urlDto = UrlDto.builder()
                .link(url)
                .build();

        return makeAndSendRequest(
                GET,
                "/playlist",
                null,
                urlDto,
                response
        );
    }

    protected void deleteImage(UrlDto url, String response) {
        makeAndSendRequest(DELETE, "/delete_image", null, url, response);
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