package main_service.playlist_card.cover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.cover.service.CoverService;
import main_service.playlist_card.playlist.dto.PlaylistSaveDto;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.dto.ReleaseUpdateDto;
import main_service.release.request.ReleaseRequest;
import org.springframework.transaction.annotation.Transactional;
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

    @ResponseBody
    @PostMapping("/generate_track")
    @Transactional
    @Operation(summary = "Request to generate cover for release")
    public ReleaseNewDto createReleaseCover(@RequestBody @Valid ReleaseRequest request,
                                            @RequestHeader(name = "Authorization", required = false) String userToken) {
        log.info("[COVERCONTROLLER] get cover for release");

        return service.getReleaseCover(userToken, request);
    }

    @PatchMapping("/regenerate_track")
    @Transactional
    @Operation(summary = "Request to regenerate cover by playlist id")
    public ReleaseUpdateDto updatePlaylistsCover(@RequestBody @Valid ReleaseRequest request,
                                                 @RequestHeader(name = "Release_Id") int releaseId,
                                                 @RequestHeader(name = "Authorization", required = false) String userToken) {
        log.info("[COVERCONTROLLER] update release cover by release Id {}", releaseId);

        return service.updateReleaseCover(request, releaseId, userToken);
    }
}