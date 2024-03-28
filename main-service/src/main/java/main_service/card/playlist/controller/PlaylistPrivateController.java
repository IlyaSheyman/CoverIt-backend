package main_service.card.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.playlist.dto.PlaylistMyCollectionDto;
import main_service.card.playlist.dto.PlaylistUserCollectionDto;
import main_service.card.playlist.service.PlaylistServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@Tag(name = "Private playlist controller", description = "For getting playlist from different sources")
public class PlaylistPrivateController {

    private final PlaylistServiceImpl service;

    @Operation(summary = "like playlist")
    @PatchMapping("/like")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void likePlaylist(@RequestHeader(name = "Authorization") String userToken,
                             @RequestHeader(value = "X-Playlist-Id") int playlistId) {
        userToken = userToken.substring(7);

        service.like(userToken, playlistId);
        //TODO
    }

    @Operation(summary = "unlike playlist")
    @PatchMapping("/unlike")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void unlikePlaylist(@RequestHeader(name = "Authorization") String userToken,
                               @RequestHeader(value = "X-Playlist-Id") int playlistId) {
        userToken = userToken.substring(7);

        service.unlikePlaylist(userToken, playlistId);
        //TODO
    }

    @Operation(summary = "get my playlists")
    @GetMapping("/my_collection")
    public Page<PlaylistMyCollectionDto> getMyPlaylists(@RequestHeader(name = "Authorization") String userToken,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("[MAIN_SERVER] get my playlist for user");
        userToken = userToken.substring(7);

        service.getMyPlaylists(userToken, page, size);

        return null; //TODO (токен нужен для того, чтобы понять - выводить private/public или нет
    }

    @Operation(summary = "get user's playlists by searching his name in 'find users'")
    @GetMapping("/user/{userId}")
    public Page<PlaylistUserCollectionDto> getUserPlaylists(@RequestHeader(name = "Authorization") String userToken,
                                                            @PathVariable(name = "userId") int userId,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("[MAIN_SERVER] get user's playlists for user");
        String requesterToken = userToken.substring(7);
        service.getUserPlaylists(requesterToken, userId,  page, size);

        return null; //TODO
    }

}