package coverit.ImageClient.client;

import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.TrackDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SpotifyClient {
    private static final String clientId = "9c5799d5294b4a2fb2b42d699177ab95";
    private static final String clientSecret = "db6d6aa567d84edf8327b27919d0258b";
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static void clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            log.info("Expires in: " + clientCredentials.getExpiresIn());
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