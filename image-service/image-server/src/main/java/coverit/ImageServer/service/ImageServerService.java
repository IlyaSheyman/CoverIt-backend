package coverit.ImageServer.service;

import coverit.ImageClient.client.ImageClient;
import coverit.ImageClient.dto.PlaylistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServerService {

    private final ImageClient client;

    public String getCoverUrl(String url) {
        getPlayListByUrl(url);
        getPromptByPlayList(null);
        getCoverByPrompt(null);

        return null;
    }

    public PlaylistDto getPlayListByUrl(String url) {
        return client.getPlayListByUrl(url);
    }

    public String getPromptByPlayList(String text) {
        return client.getPromptByPlaylist(text);
    }

    public void getCoverByPrompt(String prompt) {
        client.getCoverByPrompt();
    }
}