package coverit.ImageServer.controller;

import coverit.ImageServer.service.ImageServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageServerController {

    private final ImageServerService service;

    @GetMapping("/playlist")
    public void getPlaylistByUrl(@RequestHeader(value = "X-User-Id") int userId,
                                 @RequestParam(required = true) String url) {
        log.info("[IMAGE_SERVER] get playlist by URL {}", url);

        service.getPlayListByUrl(url, userId);
    }

    @GetMapping("/prompt")
    public void getPromptByPlaylist(@RequestBody HashMap<String, String> playlist) {
        log.info("[IMAGE_SERVER] get prompt by playlist {}", playlist.toString());

        service.getPromptByPlayList(playlist);
    }

    @GetMapping("/image")
    public void getCoverByPrompt(@RequestBody String prompt) {
        log.info("[IMAGE_SERVER] get cover by prompt {}", prompt);

        service.getCoverByPrompt(prompt);
    }
}