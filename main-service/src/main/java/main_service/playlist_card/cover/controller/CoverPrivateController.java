package main_service.playlist_card.cover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.cover.service.CoverService;
import main_service.playlist_card.playlist.dto.PlaylistSaveDto;
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
    public PlaylistSaveDto savePlaylist(@RequestHeader(value = "Playlist_Id") int playlistId,
                                        @RequestParam(name = "is_private") @NotNull Boolean isPrivate,
                                        @RequestHeader(name = "Authorization") String userToken) {
        log.info("[MAIN_SERVER] save playlist with id {}", playlistId);

        return service.savePlaylist(playlistId, isPrivate, userToken);
    }
}