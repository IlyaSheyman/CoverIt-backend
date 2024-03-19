package coverit.ImageServer.controller;

import coverit.ImageClient.constants.Constants;
import coverit.ImageClient.dto.PlaylistDto;
import coverit.ImageClient.dto.UrlDto;
import coverit.ImageServer.service.ImageServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageServerController {

    private final ImageServerService service;

    /**
     * Endpoint for generating playlist cover
     *
     * @param urlDto playlist's url
     * @param vibeString vibe of playlist (style)
     * @param isAbstract should an image be abstract or no
     * @return cover url
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cover")
    public String getCoverUrl(@RequestBody @Valid UrlDto urlDto,
                              @RequestParam(name = "vibe", required = false) String vibeString,
                              @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract) {
        String url = urlDto.getLink();
        Constants.Vibe vibe = null;

        if (vibeString != null) {
            vibe = Constants.Vibe.valueOf(vibeString.toUpperCase());
        }

        log.info("[IMAGE_SERVER] generate cover by playlist url {}", url);

        return service.getCoverUrl(url, vibe, isAbstract);
    }

    //TODO проверить isAbstract

    /**
     * Endpoint for getting playlist by spotify url
     *
     * @param urlDto playlist's url
     * @return playlist with title and tracklist
     */
    @ResponseBody
    @GetMapping("/playlist")
    public PlaylistDto getPlaylistByUrl(@RequestBody UrlDto urlDto) {
        String url = urlDto.getLink();
        System.out.println(urlDto);

        log.info("[IMAGE_SERVER] get playlist by URL {}", url);
        return service.getPlayListByUrl(url);
    }


    /**
     * Endpoint for checking interaction with ChatGpt
     *
     * @param text the request to chatGPT text
     * @return generated by chatGPT response to request text
     */
    @ResponseBody
    @GetMapping("/chatgpt")
    public String chatGPT(@RequestBody String text) {
        log.info("[IMAGE_SERVER] chatGPT request with text {}", text);

        return service.chatGpt(text);
    }

    /**
     * Endpoint for checking interaction with DALLE
     *
     * @param prompt the prompt for generating images
     * @return image url
     */
    @ResponseBody
    @GetMapping("/generate_image")
    public String generateImage(@RequestBody String prompt) {
        log.info("[IMAGE_SERVER] generate image request with prompt {}", prompt);

        return service.generateImage(prompt);
    }
}