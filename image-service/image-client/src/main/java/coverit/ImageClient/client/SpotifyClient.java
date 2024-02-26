package coverit.ImageClient.client;

import com.neovisionaries.i18n.CountryCode;
import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.TrackDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@NoArgsConstructor
@Component
public class SpotifyClient {
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("9c5799d5294b4a2fb2b42d699177ab95")
            .setClientSecret("db6d6aa567d84edf8327b27919d0258b")
            .build();

    public PlaylistDto getPlaylistByUrl(String url) {
        try {
            String playlistId = extractPlaylistIdFromUrl(url);

            GetPlaylistRequest getPlaylistRequest = spotifyApi
                    .getPlaylist(playlistId)
                    .market(CountryCode.US)
                    .build();

            Playlist playlist = getPlaylistRequest.execute();

            PlaylistDto playlistDto = PlaylistDto.builder()
                    .title(playlist.getName())
                    .tracks(getTracksFromPlaylist(playlist))
                    .build();

            return playlistDto;
        } catch (ParseException | IOException | SpotifyWebApiException e) {
            log.warn("Error: " + e.getMessage());
        }
        return null;
    }

    private ArrayList<TrackDto> getTracksFromPlaylist(Playlist playlist) {
        ArrayList<TrackDto> tracksDto = new ArrayList<>();

        for (PlaylistTrack playlistTrack : playlist.getTracks().getItems()) {
            String trackTitle = playlistTrack.getTrack().getName();
            TrackDto trackDto = TrackDto.builder()
                    .title(trackTitle)
                    .author(trackTitle) //TODO возможно имя автора и название трека хранятся в одном String,
                    // поэтому пока делаем такую реализацию, чтобы потом понять, как их разделить
                    .build();
            tracksDto.add(trackDto);
        }

        return tracksDto;
    }

    private String extractPlaylistIdFromUrl(String playlistUrl) {
        String[] urlParts = playlistUrl.split("/");
        return urlParts[urlParts.length - 1];
    }
}