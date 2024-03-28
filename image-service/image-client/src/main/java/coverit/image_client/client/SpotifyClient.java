package coverit.image_client.client;

import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.TrackDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SpotifyClient {
    private final String clientId;
    private final String clientSecret;
    private SpotifyApi spotifyApi;
    private ClientCredentialsRequest clientCredentialsRequest;

    @Autowired
    public SpotifyClient(@Value(value = "${spotify_clientId}") String clientId,
                         @Value(value = "${spotify_clientSecret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @PostConstruct
    public void init() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        clientCredentialsRequest = spotifyApi.clientCredentials().build();
    }

    public void clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.warn("Error: " + e.getMessage());
        }
    }

    public PlaylistDto getPlaylistByUrl(String url) {
        try {
            clientCredentials_Sync();

            String playlistId = extractPlaylistIdFromUrl(url);

            GetPlaylistRequest getPlaylistRequest = spotifyApi
                    .getPlaylist(playlistId)
                    .build();

            Playlist playlist = getPlaylistRequest.execute();

            PlaylistDto playlistDto = PlaylistDto.builder()
                    .title(playlist.getName())
                    .tracks(getTracksFromPlaylist(playlist))
                    .build();

            return playlistDto;
        } catch (ParseException | IOException | SpotifyWebApiException e) {
            log.warn("Error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private ArrayList<TrackDto> getTracksFromPlaylist(Playlist playlist) {

        ArrayList<TrackDto> tracksDto = new ArrayList<>();
        Paging<PlaylistTrack> playlistTrackPaging = playlist.getTracks();

        for (PlaylistTrack playlistTrack : playlistTrackPaging.getItems()) {
            Track track = (Track) playlistTrack.getTrack();
            String trackTitle = track.getName();

            ArtistSimplified[] trackAuthors = track.getArtists();
            List<String> authors = new ArrayList<>();

            for (ArtistSimplified author: trackAuthors) {
                authors.add(author.getName());
            };

            TrackDto trackDto = TrackDto.builder()
                    .title(trackTitle)
                    .authors(authors)
                    .build();

            tracksDto.add(trackDto);
        }

        return tracksDto;
    }

    private String extractPlaylistIdFromUrl(String playlistUrl) {
        String[] urlParts = playlistUrl.split("\\?")[0].split("/");
        return urlParts[urlParts.length - 1];
    }
}