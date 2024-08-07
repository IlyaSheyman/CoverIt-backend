package main_service.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.constants.Constants;
import main_service.playlist.dto.PlaylistMyCollectionDto;
import main_service.playlist.dto.PlaylistUserCollectionDto;
import main_service.playlist.service.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@Tag(name = "Private playlist controller", description = "For getting playlist from different sources")
public class PlaylistPrivateController {

    private final PlaylistService service;

    @Operation(summary = "like playlist")
    @PatchMapping("/like")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void likePlaylist(@RequestHeader(name = "Authorization") String userToken,
                             @RequestParam(name = "playlist_id") int playlistId) {
        log.info("[MAIN_SERVER] like playlist " + playlistId);
        service.like(userToken, playlistId);
    }

    @Operation(summary = "unlike playlist")
    @PatchMapping("/unlike")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void unlikePlaylist(@RequestHeader(name = "Authorization") String userToken,
                               @RequestParam(name = "playlist_id") int playlistId) {
        log.info("[MAIN_SERVER] unlike playlist " + playlistId);
        service.unlikePlaylist(userToken, playlistId);
    }

    @Operation(summary = "get my playlists")
    @GetMapping("/my_collection")
    public List<PlaylistMyCollectionDto> getMyPlaylists(@RequestHeader(name = "Authorization") String userToken,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                        @RequestParam(name = "filter", required = false) Constants.Filters filters) {
        log.info("[MAIN_SERVER] get my playlist for user");

        return service.getMyPlaylists(userToken, page, size, filters);
    }

    @Operation(summary = "get user's liked playlists")
    @GetMapping("/liked")
    public List<PlaylistMyCollectionDto> getLikedPlaylists(@RequestHeader(name = "Authorization") String userToken,
                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                           @RequestParam(name = "size", defaultValue = "10") int size,
                                                           @RequestParam(name = "filter", required = false) Constants.Filters filters)  {
        log.info("[MAIN_SERVER] get liked playlists request accepted");

        return service.getLikedPlaylists(userToken, page, size, filters);
    }

    @Operation(summary = "get user's playlists by searching his name in 'find users'")
    @GetMapping("/user_collection")
    public List<PlaylistUserCollectionDto> getUserPlaylists(@RequestHeader(name = "Authorization") String userToken,
                                                            @RequestParam(name = "user_id") int userId,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                                            @RequestParam(name = "filter", required = false) Constants.Filters filters) {
        log.info("[MAIN_SERVER] get user's playlists for user with id " + userId);

        return service.getUserPlaylists(userToken, userId,  page, size, filters);
    }

    @Operation(summary = "delete playlist")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete")
    @Transactional
    public void deletePlaylist(@RequestHeader(name = "Authorization") String userToken,
                               @RequestParam(name = "id") int playistId) {
        log.info("[MAIN_SERVER] delete playlist");

        service.deletePlaylist(userToken, playistId);
    }
}