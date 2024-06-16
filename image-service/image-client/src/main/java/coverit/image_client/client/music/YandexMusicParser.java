package coverit.image_client.client.music;

import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class YandexMusicParser {

    public PlaylistDto getPlaylistByUrl(String url) {
        Document doc = retrieveDocument(url);

        Elements trackElements = doc.select("div.d-track");
        String playlistTitle = doc.select("h1.page-playlist__title").text();

        if (trackElements.isEmpty()) {
            throw new RuntimeException("No tracks found in the playlist");
        }

        PlaylistDto playlistDto = PlaylistDto.builder().title(playlistTitle).build();
        ArrayList<TrackDto> tracksDto = new ArrayList<>();
        for (Element trackElement : trackElements) {
            String title = trackElement.select("div.d-track__name a.d-track__title").text();
            Elements artistElements = trackElement.select("div.d-track__meta a");
            List<String> artists = new ArrayList<>();

            TrackDto track = TrackDto.builder()
                    .title(title)
                    .build();

            for (Element artistElement : artistElements) {
                artists.add(artistElement.text());
            }

            track.setAuthors(artists);
            tracksDto.add(track);
        }

        playlistDto.setTracks(tracksDto);
        return playlistDto;
    }

    protected Document retrieveDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve document from URL: " + url, e);
        }
    }

}