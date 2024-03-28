package main_service.card.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.playlist.dto.PlaylistArchiveDto;
import main_service.card.playlist.service.PlaylistServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@Tag(name = "Public playlist controller", description = "For getting archive")
public class PlaylistController {

    private final PlaylistServiceImpl service;

    @Operation(summary = "get archive of playlists")
    @GetMapping("/archive")
    public Page<PlaylistArchiveDto> getArchive(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestParam(name = "sort_by", required = false) String sort) {
        log.info("[MAIN_SERVER] get my playlist for user");

        service.getArchive(page, size, sort);

        return null; //TODO
    }


}