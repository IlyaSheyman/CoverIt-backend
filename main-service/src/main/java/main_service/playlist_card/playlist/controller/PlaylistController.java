package main_service.playlist_card.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.playlist.dto.PlaylistArchiveDto;
import main_service.playlist_card.playlist.service.PlaylistServiceImpl;
import main_service.constants.Constants;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@Tag(name = "Public playlist controller", description = "For getting archive")
public class PlaylistController {

    private final PlaylistServiceImpl service;

    @Operation(summary = "get archive of playlists")
    @GetMapping("/archive")
    public List<PlaylistArchiveDto> getArchive(@RequestHeader(name = "Authorization", required = false) String userToken,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestParam(name = "filter", required = false) Constants.Filters filters) {
        log.info("[MAIN_SERVER] get playlists archive");

        return service.getArchive(page, size, filters, userToken);
    }

}