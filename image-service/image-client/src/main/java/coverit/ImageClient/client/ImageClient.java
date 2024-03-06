package coverit.ImageClient.client;

import coverit.ImageClient.constants.Constants;
import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.TrackDto;
import coverit.ImageClient.exception.BadRequestException;
import coverit.ImageClient.exception.UnsupportedRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.AiClient;
import org.springframework.stereotype.Service;

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
    public String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe, Boolean isAbstract) {
        StringBuilder request = new StringBuilder();

        if (vibe == null) {
            request.append(GPT_REQUEST_MAIN);

            if (isAbstract) {
                request.append(ABSTRACT_GPT_CONSTRAINT);
            }

            request.append(GPT_REQUEST_SETTINGS);
        } else {
            switch (vibe) {
                case DANCING_FLOOR:
                    request.append(DANCING_FLOOR_PROMPT);
                    break;
                case NATURE_DOES_NOT_CARE:
                    request.append(NATURE_PROMPT);
                    break;
                case EMPTY_SOUNDS:
                    request.append(EMPTY_SOUNDS_PROMPT);
                    break;
                case CAMPFIRE_CALMNESS:
                    request.append(CAMPFIRE_CALMNESS_PROMPT);
                    break;
                case TOUGH_AND_STRAIGHT:
                    request.append(TOUGH_AND_STRAIGHT_PROMPT);
                    break;
                default:
                    throw new BadRequestException("this vibe is not present");
            }
        }
        request.append("\n");

        for (TrackDto track: playlistDto.getTracks()) {
            request.append(track.getTitle() + "; ");
        }

        request.append("\n" + CHATGPT_SCHEME);
        log.info("request to ChatGPT: " + request);

        return aiClient.generate(request.toString());
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