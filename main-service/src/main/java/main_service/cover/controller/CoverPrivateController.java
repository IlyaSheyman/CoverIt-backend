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

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
@Tag(name = "Private cover controller", description = "For saving covers & release generation by authenticated users")
public class CoverPrivateController {
    private final CoverService service;

    @Operation(summary = "save generated playlist using playlistId and boolean isPrivate")
    @Transactional
    @PatchMapping("/playlist/save")
    public PlaylistSaveDto savePlaylist(@RequestHeader(value = "Playlist_Id") int playlistId,
                                        @RequestHeader(value = "Cover_Id") int coverId,
                                        @RequestParam(name = "is_private") @NotNull Boolean isPrivate,
                                        @RequestHeader(name = "Authorization") String userToken) {
        log.info("[MAIN_SERVER] save playlist with id {}", playlistId);

        return service.savePlaylist(playlistId, coverId, isPrivate, userToken);
    }

    @ResponseBody
    @PostMapping("/track/generate")
    @Transactional
    @Operation(summary = "Request to generate cover for release")
    public ReleaseNewDto createReleaseCover(@RequestBody @Valid ReleaseRequest request,
                                            @RequestHeader(name = "Authorization") String userToken) {
        log.info("[COVERCONTROLLER] get cover for release");

        return service.createReleaseCover(userToken, request);
    }

    @GetMapping("/track/generate/mood")
    @Operation(summary = "Request to get list of 8 adjectives that describe mood of music in release")
    public List<String> getMusicMoods(@RequestHeader(name = "Authorization") String userToken) {
        log.info("[COVERCONTROLLER] get music moods for release cover");
        return service.getMusicData("moods");
    }

    @GetMapping("/track/generate/style")
    @Operation(summary = "Request to get list of 8 styles that describe final cover of release")
    public List<String> getCoverStyles(@RequestHeader(name = "Authorization") String userToken) {
        log.info("[COVERCONTROLLER] get music styles for release cover");
        return service.getMusicData("styles");
    }
}