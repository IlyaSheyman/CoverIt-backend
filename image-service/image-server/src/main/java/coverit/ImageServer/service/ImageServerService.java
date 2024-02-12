package coverit.ImageServer.service;

import coverit.ImageClient.service.ImageClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ImageServerService {

    private final ImageClientService client;
    
    public void getPlayListByUrl() {
        client.getPlayListByUrl();
    }

    public void getPromptByPlayList(@Valid HashMap<String, String> playlist) {
        client.getPromptByPlaylist();
    }

    public void getCoverByPrompt(String prompt) {
        client.getCoverByPrompt();
    }
}
