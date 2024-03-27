package main_service.card.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.playlist.dto.PlaylistInMyCollectionDto;
import main_service.card.playlist.dto.PlaylistInUserCollectionDto;
import main_service.card.playlist.service.PlaylistServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@Tag(name = "Private playlist controller", description = "For getting playlist from different sources")
public class PlaylistPrivateController {

    private final PlaylistServiceImpl service;

    @Operation(summary = "get my playlists")
    @PatchMapping("/my_collection")
    public Page<PlaylistInMyCollectionDto> getMyPlaylists(@RequestHeader(value= "X-UserToken", required = true) String userToken,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        log.info("[MAIN_SERVER] get my playlist for user");
        userToken = userToken.substring(7);

        service.getMyPlaylists(userToken, page, size);

        return null; //TODO
    }

    @Operation(summary = "get user's playlists by searching his name in 'find users'")
    @PatchMapping("/user")
    public Page<PlaylistInUserCollectionDto> getUserPlaylists(@RequestParam(required = true) String search,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        log.info("[MAIN_SERVER] get user's playlists for user");

        service.getUserPlaylists(search, page, size);

        return null; //TODO
    }

}