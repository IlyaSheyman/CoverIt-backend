package main_service.card.cover.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.server.service.CoverService;
import main_service.card.cover.server.service.UrlDto;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class CoverPrivateController {
    private final CoverService service;

    @Operation(
            description = "save generated playlist using playlistId, imageURL, and boolean isPrivate",
            responses = {
                    @ApiResponse(
                            responseCode = "200"
                    )
            }
    )
    @PatchMapping("/cover/save")
    public void savePlaylist(@RequestHeader(value = "X-Playlist-Id") int playlistId,
                             @RequestBody UrlDto imageUrl,
                             @RequestParam(name = "is_private", required = true) @NotNull Boolean isPrivate,
                             @RequestParam(name = "user", required = true) String userToken) {
        log.info("[MAIN_SERVER] save playlist with id {}", playlistId);

        service.savePlaylist(playlistId, imageUrl, isPrivate);
    }
}