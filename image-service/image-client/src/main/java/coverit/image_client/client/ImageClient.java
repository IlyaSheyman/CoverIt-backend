package coverit.image_client.client;

import coverit.image_client.client.AI.DalleClient;
import coverit.image_client.client.music.AppleMusicParser;
import coverit.image_client.client.music.SpotifyClientStraight;
import coverit.image_client.client.music.YandexMusicParser;
import coverit.image_client.constants.Constants;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.ReleaseRequestDto;
import coverit.image_client.dto.TrackDto;
import coverit.image_client.exception.BadRequestException;
import coverit.image_client.response.CoverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.AiClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static coverit.image_client.constants.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageClient {

    private final SpotifyClientStraight spotifyClient;
    private final AppleMusicParser appleMusicParser;
    private final YandexMusicParser yandexMusicParser;

    private final AiClient aiClient;
    private final DalleClient dalleClient;

    public CoverResponse getReleaseCoverUrl(ReleaseRequestDto dto) {
        StringBuilder preGptRequest = new StringBuilder();
        preGptRequest.append("An image depicting ")
                .append(dto.getObject())
                .append(". Surrounded by ")
                .append(dto.getSurrounding())
                .append(". The picture is ")
                .append(dto.getCoverDescription().toString());

        log.info("Pre-gpt prompt: " + preGptRequest);

        String finalRequest = chatGptReleasePromptBuild(dto, preGptRequest);

        log.info("Post-gpt prompt: " + finalRequest);

        String imageUrl;

        if (dto.getIsLoFi()) {
             imageUrl = dalleClient.generateImage(finalRequest, "dall-e-2");
        } else {
            imageUrl = dalleClient.generateImage(finalRequest, "dall-e-3");
        }

        return CoverResponse.builder()
                .url(imageUrl)
                .prompt(finalRequest)
                .build();
    }

    private String chatGptReleasePromptBuild(ReleaseRequestDto dto, StringBuilder requestToDalle) {
        StringBuilder requestToGpt = new StringBuilder();
        requestToGpt.append("Add to the visual AI prompt details (3-5 words) that reflect the mood (these can be adjectives, objects or actions): ")
                .append(dto.getMood().toString())
                .append(". Prompt: ")
                .append(requestToDalle)
                .append(". Your answer should include full updated prompt.");

        log.info("Request to gpt: " + requestToGpt);

        return aiClient.generate(requestToGpt.toString());
    }

    public PlaylistDto getPlayListByUrl(String url) throws ExecutionException, InterruptedException {
        if (url.matches(YANDEX_MUSIC_REGEX)) {
            PlaylistDto playlistDto = yandexMusicParser.getPlaylistByUrl(url);
            log.info(playlistDto.toString());
            return playlistDto;
        } else if (url.matches(APPLE_MUSIC_REGEX)) {
            PlaylistDto playlistDto = appleMusicParser.getPlaylistByUrl(url);
            log.info(playlistDto.toString());
            return playlistDto;
        } else if (url.matches(Constants.SPOTIFY_REGEX)) {
            PlaylistDto playlistDto = spotifyClient.getPlaylistByUrlAsync(url).get();
            log.info(playlistDto.toString());
            return playlistDto;
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
            request.append(extractTitle(track.getTitle())).append("; ");
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
                    request.append(Constants.GPT_NATURE);
                    break;
                case BREAKING_DOWN:
                    request.append(Constants.GPT_BREAKING_DOWN);
                    break;
                case CAMPFIRE_CALMNESS:
                    request.append(Constants.GPT_CAMPFIRE_CALMNESS);
                    break;
                case TOUGH_AND_STRAIGHT:
                    request.append(Constants.GPT_TOUGH_AND_STRAIGHT);
                    break;
                case ENDLESS_JOY:
                    request.append(GPT_ENDLESS_JOY);
                    break;
                default:
                    throw new BadRequestException("this vibe is not present");
            }
        }

        request.append(GPT_SETTINGS_AVOID);
        request.append(Constants.GPT_SETTINGS_LENGTH);
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

    public String getCoverByPrompt(String prompt, Boolean isLoFi) {
        if (isLoFi) {
            return dalleClient.generateImage(prompt, "dall-e-2");
        } else {
            return dalleClient.generateImage(prompt, "dall-e-3");
        }
    }

    public String generateImage(String prompt) {
        return dalleClient.generateImage(prompt, "dall-e-2");
    }

    public String chatGpt(String text) {
        return aiClient.generate(text);
    }

}