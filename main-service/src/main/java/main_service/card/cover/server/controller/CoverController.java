package main_service.card.cover.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.server.service.CoverService;
import main_service.card.cover.server.service.UrlDto;
import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.constants.Constants;
import main_service.user.dto.UserCreateDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
public class CoverController {

    private final CoverService service;


    @PostMapping("/generate")
    public PlaylistNewDto createPlaylistWithCover(@RequestBody @Valid UrlDto url,
                                                  @RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                                  @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                                  @RequestHeader(value = "X-User-Id") int userId) {
        log.info("[MAIN_SERVER] get cover by playlist URL {}", url.getLink());

        return service.getCover(userId, url, vibe, isAbstract);
    }

    @Operation(
            description = "save generated playlist using playlistId, imageURL, and boolean isPrivate",
            responses = {
                    @ApiResponse(
                            responseCode = "200"
                    )
            }
    )
    @PatchMapping("/save")
    public void savePlaylist(@RequestHeader(value = "X-Playlist-Id") int playlistId,
                             @RequestBody UrlDto imageUrl,
                             @RequestParam(name = "is_private", required = true) @NotNull Boolean isPrivate) {
        log.info("[MAIN_SERVER] save playlist with id {}", playlistId);

        service.savePlaylist(playlistId, imageUrl, isPrivate);
    }
}