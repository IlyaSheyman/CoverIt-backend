package coverit.ImageServer.controller;

import coverit.ImageClient.constants.Constants;
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

    @PostMapping("/cover")
    public String getCoverUrl(@RequestBody @Valid UrlDto urlDto,
                              @RequestParam(required = false) Constants.Vibe vibe) {
        String url = urlDto.getLink();
        log.info("[IMAGE_SERVER] generate cover by playlist url {}", url);

        return service.getCoverUrl(url, vibe);
    }


    //Эндпоинт для проверки получения плейлиста по url
    @ResponseBody
    @GetMapping("/playlist")
    public PlaylistDto getPlaylistByUrl(@RequestBody @Valid UrlDto urlDto) {
        String url = urlDto.getLink();

        log.info("[IMAGE_SERVER] get playlist by URL {}", url);
        return service.getPlayListByUrl(url);
    }

    //Эндпоинт для проверки взаимодействия с ChatGpt
    @ResponseBody
    @GetMapping("/chatgpt")
    public String chatGPT(@RequestBody String text) {
        log.info("[IMAGE_SERVER] chatGPT request with text {}", text);

        return service.chatGpt(text);
    }

    //Эндпоинт для проверки взаимодействия с DALL-E
    @ResponseBody
    @GetMapping("/generate_image")
    public String generateImage(@RequestBody String prompt) {
        log.info("[IMAGE_SERVER] generate image request with prompt {}", prompt);

        return service.generateImage(prompt);
    }
}