package coverit.ImageClient.client;

import coverit.ImageClient.constants.Constants;
import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.TrackDto;
import coverit.ImageClient.exception.BadRequestException;
import coverit.ImageClient.exception.UnsupportedRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static coverit.ImageClient.constants.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageClient {

    private final SpotifyClient spotifyClient;
    private final AiClient aiClient;
    private final DalleClient dalleClient;

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
    public String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe) {
        StringBuilder request = new StringBuilder();

        if (vibe == null) {
            request.append(NONE_VIBE_PROMPT);
            request.append("\n");

            for (TrackDto track: playlistDto.getTracks()) {
                request.append(track.getTitle() + ", ");
            }

            return aiClient.generate(request.toString());
        } else {
            switch (vibe) {
                case DANCING_FLOOR:
                    return null;
                case NATURE_DOES_NOT_CARE:
                    return null;
                case SOUND_OF_NOTHING:
                    return null;
                case CAMPFIRE_CALMNESS:
                    return null;
                case TOUGH_AND_STRAIGHT:
                    return null;
                case GARDEN_OF_NOSTALGIA:
                    return null;
                case FUTURE_IS_NOW:
                    return null;
                case ROUTINE_SOUNDS:
                    return null;
                default:
                    throw new BadRequestException("this vibe is not present");
            }
        }
    }

    public String getCoverByPrompt(String prompt) {
        return dalleClient.generateImage(prompt);
    }


    public String generateImage(String prompt) {
        return dalleClient.generateImage(prompt);
    }

    public String chatGpt(String text) {
        return aiClient.generate(text);
    }
}