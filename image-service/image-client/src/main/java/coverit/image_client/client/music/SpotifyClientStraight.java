package coverit.image_client.client.music;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.TrackDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SpotifyClientStraight {
    private final String clientId;
    private final String clientSecret;
    private final RestTemplate restTemplate;
    private SpotifyApi spotifyApi;
    private ClientCredentialsRequest clientCredentialsRequest;

    @Autowired
    public SpotifyClientStraight(@Value(value = "${spotify_clientId}") String clientId,
                                 @Value(value = "${spotify_clientSecret}") String clientSecret,
                                 RestTemplateBuilder builder) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = builder.build();
    }

    @PostConstruct
    public void init() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        clientCredentialsRequest = spotifyApi.clientCredentials().build();
    }

    public CompletableFuture<PlaylistDto> getPlaylistByUrlAsync(String url) {
        return clientCredentialsRequest.executeAsync()
                .thenApplyAsync(clientCredentials -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(clientCredentials.getAccessToken());
                    HttpEntity<Void> request = new HttpEntity<>(headers);

                    String playlistId = extractPlaylistIdFromUrl(url);
                    String apiUrl = "https://api.spotify.com/v1/playlists/" + playlistId;

                    ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl,
                            HttpMethod.GET,
                            request,
                            String.class);

                    String responseBodyString = responseEntity.getBody();
                    Gson gson = new Gson();
                    JsonObject responseBodyJson = gson.fromJson(responseBodyString, JsonObject.class);

                    ArrayList<TrackDto> tracks = extractTracksFromJson(responseBodyJson);
                    String title = responseBodyJson.get("name").getAsString();

                    return PlaylistDto.builder()
                            .title(title)
                            .tracks(tracks)
                            .build();
                });
    }

    private ArrayList<TrackDto> extractTracksFromJson(JsonObject response) {
        JsonObject tracksJson = response.getAsJsonObject("tracks");
        JsonArray itemsArray = tracksJson.getAsJsonArray("items");
        ArrayList<TrackDto> trackList = new ArrayList<>();

        for (JsonElement itemElement : itemsArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            JsonObject trackObject = itemObject.getAsJsonObject("track");
            String trackName = trackObject.get("name").getAsString();
            JsonArray artistsArray = trackObject.getAsJsonArray("artists");

            List<String> artistsNames = new ArrayList<>();

            for (JsonElement artistElement : artistsArray) {
                JsonObject artistObject = artistElement.getAsJsonObject();
                String artistName = artistObject.get("name").getAsString();
                artistsNames.add(artistName);
            }

            TrackDto track = TrackDto.builder()
                    .authors(artistsNames)
                    .title(trackName)
                    .build();

            trackList.add(track);
        }

        return trackList;
    }

    private String extractPlaylistIdFromUrl(String playlistUrl) {
        String[] urlParts = playlistUrl.split("\\?")[0].split("/");
        return urlParts[urlParts.length - 1];
    }
}
