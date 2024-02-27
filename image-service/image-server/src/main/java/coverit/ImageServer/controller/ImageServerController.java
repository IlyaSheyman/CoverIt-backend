package coverit.ImageServer.controller;

import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.UrlDto;
import coverit.ImageServer.service.ImageServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageServerController {

    private final ImageServerService service;

    @GetMapping("/cover")
    public String getCoverUrl(@RequestParam(required = true) String url) {
        log.info("[IMAGE_SERVER] generate cover by playlist url {}", url);

        return service.getCoverUrl(url);
    }


    //Следующий эндпоинт добавлен для проверки получения плейлиста по url
    @ResponseBody
    @GetMapping("/playlist")
    public PlaylistDto getPlaylistByUrl(@RequestBody @Valid UrlDto urlDto) {
        String url = urlDto.getLink();

        log.info("[IMAGE_SERVER] get playlist by URL {}", url);
        return service.getPlayListByUrl(url);
    }

    @ResponseBody
    @GetMapping("/chatgpt")
    public String chatGPT(@RequestBody String text) {
        log.info("[IMAGE_SERVER] chatGPT request with text {}", text);

        return service.getPromptByPlayList(text);
    }
}