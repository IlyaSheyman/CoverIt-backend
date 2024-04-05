package coverit.image_service.service;

import coverit.image_client.client.ImageClient;
import coverit.image_client.constants.Constants;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.ReleaseRequestDto;
import coverit.image_client.dto.TrackDto;
import coverit.image_client.exception.BadRequestException;
import coverit.image_client.response.CoverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static coverit.image_client.constants.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
@ComponentScan("coverit.image_client.client")
public class ImageServerService {

    private final ImageClient client;

    public CoverResponse getPlaylistCoverUrl(String url, Vibe vibe, Boolean isAbstract, Boolean isLoFi) {
        PlaylistDto playlistDto = getPlayListByUrl(url);
        log.info("playlist name: " + playlistDto.getTitle());
        String prompt = getPromptByPlaylist(playlistDto, vibe, isAbstract);
        String imageUrl = getCoverByPrompt(prompt, isLoFi);
        log.info("image url: " + imageUrl);

        CoverResponse response = CoverResponse.builder()
                .prompt(prompt)
                .url(imageUrl)

                .build();
        return response;
    }

    private String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe, Boolean isAbstract) {
        if (isAbstract == null) {
            throw new BadRequestException("parameter isAbstract is not present");
        }

        if (playlistDto.getTracks().size() > MAX_PLAYLIST_SIZE) {
            selectTracksFromPlaylist(playlistDto);
        }
        String gptResult = client.getPromptByPlaylist(playlistDto, vibe, isAbstract);

        StringBuilder prompt = new StringBuilder();
        prompt.append("An image depicting: ");
        prompt.append(gptResult + " ");

        if (vibe == null) {
            prompt.append(NONE_VIBE_DALLE_INSTRUCTION);
        } else {
            switch (vibe) {
                case DANCING_FLOOR:
                    prompt.append(DANCING_FLOOR_INSTRUCTION);
                    break;
                case NATURE_DOES_NOT_CARE:
                    prompt.append(NATURE_INSTRUCTION);
                    break;
                case BREAKING_DOWN:
                    prompt.append(GPT_BREAKING_DOWN);
                    break;
                case CAMPFIRE_CALMNESS:
                    prompt.append(CAMPFIRE_CALMNESS_PROMPT);
                    break;
                case TOUGH_AND_STRAIGHT:
                    prompt.append(TOUGH_AND_STRAIGHT_PROMPT);
                    break;
                default:
                    throw new BadRequestException("this vibe is not present");
            }
        }

        log.info("prompt for DALLE: " + prompt);

        return prompt.toString();
    }

    private void selectTracksFromPlaylist(PlaylistDto playlistDto) {
        List<TrackDto> tracks = playlistDto.getTracks();
        ArrayList<TrackDto> selectedTracks = new ArrayList<>();

        int step = tracks.size() / (MAX_PLAYLIST_SIZE + 1);

        for (int i = step; i < tracks.size() && selectedTracks.size() < MAX_PLAYLIST_SIZE; i += step) {
            selectedTracks.add(tracks.get(i));
        }

        playlistDto.getTracks().removeAll(tracks);
        playlistDto.setTracks(selectedTracks);

        log.info(MAX_PLAYLIST_SIZE + " tracks were selected from the playlist. current playlist size: "
                + playlistDto.getTracks().size());
    }

    public String getReleaseCoverUrl(ReleaseRequestDto request) {
        String imageUrl = client.getReleaseCoverUrl(request);
        log.info("image url: " + imageUrl);

        return imageUrl;
    }

    public PlaylistDto getPlayListByUrl(String url) {
        return client.getPlayListByUrl(url);
    }

    public String getCoverByPrompt(String prompt, Boolean isLoFi) {
        return client.getCoverByPrompt(prompt, isLoFi);
    }

    public String generateImage(String prompt) {
        return client.generateImage(prompt);
    }

    public String chatGpt(String text) {
        return client.chatGpt(text);
    }
}