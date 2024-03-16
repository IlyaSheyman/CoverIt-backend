package main_service.card.cover.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.server.service.CoverService;
import main_service.card.cover.server.service.UrlDto;
import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.card.playlist.dto.PlaylistUpdateDto;
import main_service.constants.Constants;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
@Tag(name = "Public cover controller", description = "For covers generation and regeneration")
public class CoverController {

    private final CoverService service;


    @PostMapping("/generate")
    @Operation(summary = "Request to generate cover")
    public PlaylistNewDto createPlaylistWithCover(@RequestBody @Valid UrlDto url,
                                                  @RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                                  @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                                  @RequestHeader(name = "Authorization", required = false) String userToken) {
        log.info("[MAIN_SERVER] get cover by playlist URL {}", url.getLink());

        return service.getCover(userToken, url, vibe, isAbstract);
    }

    @PatchMapping("/regenerate")
    @Operation(summary = "Request to regenerate cover by playlist id")
    public PlaylistUpdateDto updatePlaylistsCover(@RequestBody @Valid UrlDto url,
                                                  @RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                                  @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                                  @RequestHeader(name = "Playlist_Id", required = true) int playlistId) {
        log.info("[MAIN_SERVER] update cover by playlist URL {}", url.getLink());

        return service.updateCover(vibe, isAbstract, playlistId);
    }

}