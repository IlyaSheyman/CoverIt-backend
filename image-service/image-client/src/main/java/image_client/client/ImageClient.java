package image_client.client;

import image_client.constants.Constants;
import image_client.dto.PlaylistDto;
import image_client.dto.TrackDto;
import image_client.exception.BadRequestException;
import image_client.exception.UnsupportedRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.AiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageClient {

    private final SpotifyClient spotifyClient;
    private final AiClient aiClient;
    private final DalleClient dalleClient;

    public PlaylistDto getPlayListByUrl(String url) {
        //на долгий срок: TODO добавить получение плейлиста из Яндекс Музыки, отдельный клиент
        if (url.matches(Constants.SPOTIFY_REGEX)) {
            return spotifyClient.getPlaylistByUrl(url);
        } else if (url.matches(Constants.YANDEX_MUSIC_REGEX)) {
            throw new UnsupportedRequestException("yandex music playlist is not supported yet");
        } else {
            throw new BadRequestException("incorrect url");
        }
    }
    public String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe, Boolean isAbstract) {
        StringBuilder request = new StringBuilder();

        request.append(Constants.GPT_REQUEST_MAIN);

        if (isAbstract) {
            request.append(Constants.ABSTRACT_GPT_CONSTRAINT);
        }

        request.append("\n");

        for (TrackDto track: playlistDto.getTracks()) {
            request.append(extractTitle(track.getTitle()) + "; ");
        }

        request.append("\n");

        if (vibe == null) {
            request.append("\n" + Constants.GPT_NONE_VIBE);
        } else {
            switch (vibe) {
                case DANCING_FLOOR:
                    request.append(Constants.GPT_DANCING_FLOOR);
                    break;
                case NATURE_DOES_NOT_CARE:
                    request.append(Constants.NATURE_PROMPT);
                    break;
                case EMPTY_SOUNDS:
                    request.append(Constants.EMPTY_SOUNDS_PROMPT);
                    break;
                case CAMPFIRE_CALMNESS:
                    request.append(Constants.CAMPFIRE_CALMNESS_PROMPT);
                    break;
                case TOUGH_AND_STRAIGHT:
                    request.append(Constants.TOUGH_AND_STRAIGHT_PROMPT);
                    break;
                default:
                    throw new BadRequestException("this vibe is not present");
            }
        }

        request.append(Constants.GPT_REQUEST_SETTINGS);
        log.info("request to ChatGPT: " + request);

        return aiClient.generate(request.toString());
    }

    private String extractTitle(String title) {
        if (title.contains("(") && title.contains(")")) {
            int startIndex = title.indexOf("(");
            int endIndex = title.indexOf(")");

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                title = title.substring(0, startIndex) + title.substring(endIndex + 1);
            }
        }

        if (title.contains("[") && title.contains("]")) {
            int startIndex = title.indexOf("[");
            int endIndex = title.indexOf("]");

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                title = title.substring(0, startIndex) + title.substring(endIndex + 1);
            }
        }

        return title;
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