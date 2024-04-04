package coverit.image_service.controller;

import coverit.image_client.constants.Constants;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.ReleaseRequestDto;
import coverit.image_client.dto.UrlDto;
import coverit.image_client.response.CoverResponse;
import coverit.image_service.service.ImageServerService;
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
    @PostMapping("/cover_playlist")
    public CoverResponse getPlaylistCoverUrl(@RequestBody @Valid UrlDto urlDto,
                                             @RequestParam(name = "vibe", required = false) String vibeString,
                                             @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                             @RequestParam(name = "is_lofi", defaultValue = "true") Boolean isLoFi) { //TODO сменить на стринг попробовать, чтобы пофиксить ошибку
        String url = urlDto.getLink();

        Constants.Vibe vibe = null;
        if (!vibeString.equals("none")) {
            vibe = Constants.Vibe.valueOf(vibeString.toUpperCase());
        }

        log.info("[IMAGE_SERVER] generate cover by playlist url {}", url);

        return service.getPlaylistCoverUrl(url, vibe, isAbstract, isLoFi);
    }

    /**
     * Endpoint for getting cover for release
     *
     * @param request release's properties (of music and cover)
     * @return cover url
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cover_release")
    public String getReleaseCoverUrl(@RequestBody ReleaseRequestDto request) {

        log.info("[IMAGE_SERVER] generate cover for release");

        return service.getReleaseCoverUrl(request);
    }

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