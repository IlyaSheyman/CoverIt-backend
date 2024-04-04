package main_service.playlist_card.cover.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.cover.service.UrlDto;
import main_service.playlist_card.playlist.dto.CoverResponse;
import main_service.playlist_card.playlist.dto.PlaylistDto;
import main_service.constants.Constants;
import main_service.release.dto.ReleaseRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@Slf4j
public class CoverClient extends HttpClient {

    @Autowired
    public CoverClient(@Value("${image-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public String createReleaseCover(ReleaseRequestDto releaseRequestDto) {
        log.debug("[COVER CLIENT] sending request to generate cover for release to Image-Service");

        ResponseEntity<String> res = this.coverRelease(releaseRequestDto, new String());

        if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
            return res.getBody().toString();
        } else {
            throw new RuntimeException("incorrect response from image-server. http status: "
                    + res.getStatusCode());
        }
    }

    public CoverResponse createPlaylistCover(UrlDto urlDto, Constants.Vibe vibe, Boolean isAbstract, Boolean isLoFi) {
        log.debug("[COVER CLIENT] sending request to generate cover for playlist {} to Image-Service",
                urlDto.getLink());

        ResponseEntity<CoverResponse> res = this.coverPlaylist(urlDto, vibe, isAbstract, isLoFi, new CoverResponse());

        if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(
                        mapper.writeValueAsString(res.getBody()),
                        CoverResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }

        } else {
            throw new RuntimeException("incorrect response from image-server. http status: "
                    + res.getStatusCode());
        }
    }

    public PlaylistDto getPlaylist(String url) {

        ResponseEntity<PlaylistDto> response = this.getPlaylistDto(url, new PlaylistDto());

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(
                        mapper.writeValueAsString(response.getBody()),
                        PlaylistDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }
}