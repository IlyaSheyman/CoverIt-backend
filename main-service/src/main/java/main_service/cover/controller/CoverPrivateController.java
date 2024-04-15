package main_service.cover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.cover.service.CoverService;
import main_service.playlist.dto.PlaylistSaveDto;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.request.ReleaseRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
@Tag(name = "Private cover controller", description = "For saving covers & release generation by authenticated users")
public class CoverPrivateController {
    private final CoverService service;

    @Operation(summary = "save generated playlist using playlistId and boolean isPrivate")
    @PatchMapping("/playlist/save")
    public PlaylistSaveDto savePlaylist(@RequestHeader(value = "Playlist_Id") int playlistId,
                                        @RequestParam(name = "is_private") @NotNull Boolean isPrivate,
                                        @RequestHeader(name = "Authorization") String userToken) {
        log.info("[MAIN_SERVER] save playlist with id {}", playlistId);

        return service.savePlaylist(playlistId, isPrivate, userToken);
    }

    @ResponseBody
    @PostMapping("/track/generate")
    @Transactional
    @Operation(summary = "Request to generate cover for release")
    public ReleaseNewDto createReleaseCover(@RequestBody @Valid ReleaseRequest request,
                                            @RequestHeader(name = "Authorization") String userToken) {
        log.info("[COVERCONTROLLER] get cover for release");

        return service.getReleaseCover(userToken, request);
    }
}