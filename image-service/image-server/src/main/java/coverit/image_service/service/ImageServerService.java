package coverit.image_service.service;

import coverit.image_client.client.ImageClient;
import coverit.image_client.constants.Constants;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.TrackDto;
import coverit.image_client.exception.BadRequestException;
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

    public String getCoverUrl(String url, Constants.Vibe vibe, Boolean isAbstract) {
//        PlaylistDto playlistDto = getPlayListByUrl(url);
//        log.info("playlist name: " + playlistDto.getTitle());
//        String prompt = getPromptByPlaylist(playlistDto, vibe, isAbstract);
//        String imageUrl = getCoverByPrompt(prompt);
//        log.info("image url: " + imageUrl);
//
//        return imageUrl;

        String draftForDev = "https://oaidalleapiprodscus.blob.core.windows.net/private/org-g5uQeMCnQ8Fid3HaD7A568qD/user-MpCFiOesHOu11BWSiajNCsWv/img-IAcWztZv4bOa2BBIS2OeylWm.png?st=2024-03-26T22%3A45%3A28Z&se=2024-03-27T00%3A45%3A28Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2024-03-26T21%3A22%3A39Z&ske=2024-03-27T21%3A22%3A39Z&sks=b&skv=2021-08-06&sig=68HShPjpREP2HaRtDHBH/6rIew749bE3HAGC9VMDptY%3D";
        return draftForDev;
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
                    prompt.append(NATURE_PROMPT);
                    break;
                case BREAKING_DOWN:
                    prompt.append(EMPTY_SOUNDS_PROMPT);
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

    public PlaylistDto getPlayListByUrl(String url) {
        return client.getPlayListByUrl(url);
    }

    public String getCoverByPrompt(String prompt) {
        return client.getCoverByPrompt(prompt);
    }

    public String generateImage(String prompt) {
        return client.generateImage(prompt);
    }

    public String chatGpt(String text) {
        return client.chatGpt(text);
    }
}