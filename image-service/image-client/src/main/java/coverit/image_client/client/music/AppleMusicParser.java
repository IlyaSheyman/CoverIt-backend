package coverit.image_client.client.music;

import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleMusicParser {

    public PlaylistDto getPlaylistByUrl(String url) {
        Document doc = retrieveDocument(url);
        Element webData = doc.selectFirst("script[id=serialized-server-data]");

        if (webData == null) {
            throw new RuntimeException("Serialized server data not found in HTML");
        }

        JSONArray jsonWebData = new JSONArray(webData.data());

        JSONObject playlistData = jsonWebData
                .getJSONObject(0)
                .getJSONObject("data")
                .getJSONObject("seoData");

        return buildPlaylistDto(playlistData);
    }

    private PlaylistDto buildPlaylistDto(JSONObject data) {
        String playlistTitle = data.getString("appleTitle");
        JSONArray tracks = data.getJSONArray("ogSongs");
        ArrayList<TrackDto> tracksDto = extractTracks(tracks);

        return PlaylistDto.builder()
                .title(playlistTitle)
                .tracks(tracksDto)
                .build();
    }

    private ArrayList<TrackDto> extractTracks(JSONArray tracks) {
        ArrayList<TrackDto> tracksDto = new ArrayList<>();
        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = tracks.getJSONObject(i).getJSONObject("attributes");
            String trackTitle = track.getString("name");
            List<String> authors = extractTracksAuthors(track);

            TrackDto trackDto = TrackDto.builder()
                    .authors(authors)
                    .title(trackTitle)
                    .build();

            tracksDto.add(trackDto);
        }
        return tracksDto;
    }

    protected Document retrieveDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve document from URL: " + url, e);
        }
    }

    private List<String> extractTracksAuthors(JSONObject track) {
        String authorsString = track.getString("artistName");
        return Arrays.asList(authorsString.split(" & "));
    }

}