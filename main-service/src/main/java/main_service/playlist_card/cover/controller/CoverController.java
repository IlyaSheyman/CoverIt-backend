package main_service.playlist_card.cover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.cover.service.CoverService;
import main_service.playlist_card.cover.service.UrlDto;
import main_service.playlist_card.playlist.dto.PlaylistNewDto;
import main_service.playlist_card.playlist.dto.PlaylistUpdateDto;
import main_service.constants.Constants;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.request.ReleaseRequest;
import main_service.release.dto.ReleaseUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
@Tag(name = "Public cover controller", description = "For covers generation and regeneration")
public class CoverController {

    private final CoverService service;


    @PostMapping("/generate_playlist")
    @Transactional
    @Operation(summary = "Request to generate cover for playlist")
    public PlaylistNewDto createPlaylistWithCover(@RequestBody @Valid UrlDto url,
                                                  @RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                                  @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                                  @RequestParam(name = "is_lofi", defaultValue = "true") Boolean isLoFi,
                                                  @RequestHeader(name = "Authorization", required = false) String userToken) {
        log.info("[COVERCONTROLLER] get cover by playlist URL {}", url.getLink());

        return service.getCover(userToken, url, vibe, isAbstract, isLoFi);
    }

    @PatchMapping("/regenerate_playlist")
    @Transactional
    @Operation(summary = "Request to regenerate cover by playlist id")
    public PlaylistUpdateDto updatePlaylistsCover(@RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                                  @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                                  @RequestHeader(name = "Playlist_Id") int playlistId,
                                                  @RequestHeader(name = "Authorization", required = false) String userToken) {
        log.info("[COVERCONTROLLER] update cover by playlist Id {}", playlistId);

        return service.updateCover(vibe, isAbstract, playlistId, userToken);
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