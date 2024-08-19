package main_service.cover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.constants.Constants;
import main_service.cover.dto.RandomCoversDto;
import main_service.cover.entity.Cover;
import main_service.cover.service.CoverService;
import main_service.cover.service.UrlDto;
import main_service.playlist.dto.PlaylistNewDto;
import main_service.playlist.dto.PlaylistUpdateDto;
import main_service.playlist.request.PlaylistRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
@Tag(name = "Public cover controller", description = "For covers generation and regeneration")
public class CoverController {

    private final CoverService service;

    @PostMapping("/playlist/generate")
    @Transactional
    @Operation(summary = "Request to generate cover for playlist")
    public PlaylistNewDto createPlaylistWithCover(@RequestBody @Valid PlaylistRequest request,
                                                  @RequestHeader(name = "Authorization", required = false) String userToken) {
        log.info("[COVERCONTROLLER] get cover by playlist URL {}", request.getUrlDto().getLink());

        return service.createPlaylistCover(userToken, request);
    }

    @PatchMapping("/playlist/regenerate")
    @Transactional
    @Operation(summary = "Request to regenerate cover by playlist id")
    public PlaylistUpdateDto updatePlaylistsCover(@RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                                  @RequestParam(name = "is_abstract", defaultValue = "false") Boolean isAbstract,
                                                  @RequestParam(name = "playlist_id") int playlistId,
                                                  @RequestHeader(name = "Authorization", required = false) String userToken,
                                                  @RequestParam(name = "is_lofi", defaultValue = "true") Boolean isLoFi) {
        log.info("[COVERCONTROLLER] update cover by playlist Id {}", playlistId);

        return service.updatePlaylistCover(vibe, isAbstract, playlistId, userToken, isLoFi);
    }
}