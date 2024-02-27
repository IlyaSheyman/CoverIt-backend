package coverit.ImageClient.client;

import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.exception.BadRequestException;
import coverit.ImageClient.exception.UnsupportedRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static coverit.ImageClient.constants.Constants.SPOTIFY_REGEX;
import static coverit.ImageClient.constants.Constants.YANDEX_MUSIC_REGEX;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageClient {

    private final SpotifyClient spotifyClient;
    private final ChatGptClient chatGptClient;

    public PlaylistDto getPlayListByUrl(String url) {
        //на долгий срок: TODO добавить получение плейлиста из Яндекс Музыки, отдельный клиент
        if (url.matches(SPOTIFY_REGEX)) {
            return spotifyClient.getPlaylistByUrl(url);
        } else if (url.matches(YANDEX_MUSIC_REGEX)) {
            throw new UnsupportedRequestException("yandex music playlist is not supported yet");
        } else {
            throw new BadRequestException("incorrect url");
        }
    }

    public String getPromptByPlaylist(String text) {
        return chatGptClient.chatGPT(text);
    }

    public void getCoverByPrompt() {
    }
}
