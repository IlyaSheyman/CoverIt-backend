package main_service.card.cover.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Private cover controller", description = "For saving covers by authenticated users")
public class CoverPrivateController {
    private final CoverService service;

    @Operation(summary = "save generated playlist using playlistId and boolean isPrivate")
    @PatchMapping("/cover/save")
    public void savePlaylist(@RequestHeader(value = "X-Playlist-Id") int playlistId,
                             @RequestParam(name = "is_private", required = true) @NotNull Boolean isPrivate,
                             @RequestHeader(value = "X-UserToken", required = true) String userToken) {
        log.info("[MAIN_SERVER] save playlist with id {}", playlistId);
        userToken = userToken.substring(7);
        service.savePlaylist(playlistId, isPrivate, userToken);
    }
}