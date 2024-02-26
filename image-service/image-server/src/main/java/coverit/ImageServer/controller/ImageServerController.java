package coverit.ImageServer.controller;

import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageServer.service.ImageServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public PlaylistDto getPlaylistByUrl(@RequestParam(required = true) String url) {
        log.info("[IMAGE_SERVER] get playlist by URL {}", url);

        return service.getPlayListByUrl(url);
    }
}