package coverit.ImageServer.service;

import coverit.ImageClient.client.ImageClient;
import coverit.ImageClient.constants.Constants;
import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.TrackDto;
import coverit.ImageClient.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static coverit.ImageClient.constants.Constants.MAX_PLAYLIST_SIZE;
import static coverit.ImageClient.constants.Constants.NONE_VIBE_DALLE_INSTRUCTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServerService {

    private final ImageClient client;

    public String getCoverUrl(String url, Constants.Vibe vibe, Boolean isAbstract) {
        PlaylistDto playlistDto = getPlayListByUrl(url);
        log.info("playlist name: " + playlistDto.getTitle());
        String prompt = getPromptByPlaylist(playlistDto, vibe, isAbstract);
        return getCoverByPrompt(prompt);
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
            return null; //TODO добавить инструкции для других вайбов
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