package coverit.ImageServer.service;

import coverit.ImageClient.client.ImageClient;
import coverit.ImageClient.constants.Constants;
import coverit.ImageClient.dto.PlaylistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServerService {

    private final ImageClient client;

    public String getCoverUrl(String url, Constants.Vibe vibe) {
        PlaylistDto playlistDto = getPlayListByUrl(url);
        getPromptByPlaylist(playlistDto, vibe);
        getCoverByPrompt(null);

        return null;
    }

    private String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe) {
        return null;
    }

    public PlaylistDto getPlayListByUrl(String url) {
        return client.getPlayListByUrl(url);
    }

    public String chatGpt(String text) {
        return client.chatGpt(text);
    }

    public void getCoverByPrompt(String prompt) {
        client.getCoverByPrompt();
    }
}