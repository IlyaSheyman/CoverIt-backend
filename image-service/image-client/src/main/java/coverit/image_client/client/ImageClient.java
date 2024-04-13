package coverit.image_client.client;

import coverit.image_client.constants.Constants;
import coverit.image_client.dto.ReleaseRequestDto;
import coverit.image_client.exception.BadRequestException;
import coverit.image_client.exception.UnsupportedRequestException;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.TrackDto;
import coverit.image_client.response.CoverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.AiClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageClient {

    private final SpotifyClient spotifyClient;
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
        requestToGpt.append("Add to the visual AI prompt details (3-5 words) that reflect the mood: ")
                .append(dto.getMood().toString())
                .append(". Prompt: ")
                .append(requestToDalle)
                .append(". Your answer should include full updated prompt.");

        log.info("Request to gpt: " + requestToGpt);

        return aiClient.generate(requestToGpt.toString());
    }

    public PlaylistDto getPlayListByUrl(String url) throws ExecutionException, InterruptedException {
        //на долгий срок: TODO добавить получение плейлиста из Яндекс Музыки, отдельный клиент
        if (url.matches(Constants.SPOTIFY_REGEX)) {
            return spotifyClient.getPlaylistByUrlAsync(url).get();
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
                    request.append(Constants.GPT_NATURE);
                    break;
                case BREAKING_DOWN:
                    request.append(Constants.GPT_BREAKING_DOWN);
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