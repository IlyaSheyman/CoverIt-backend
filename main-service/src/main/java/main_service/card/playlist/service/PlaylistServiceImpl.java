package main_service.card.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.storage.CoverRepository;
import main_service.card.playlist.entity.Playlist;
import main_service.card.playlist.mapper.PlaylistMapper;
import main_service.card.playlist.storage.PlaylistRepository;
import main_service.card.track.storage.TrackRepository;
import main_service.config.security.JwtService;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistServiceImpl {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final JwtService jwtService;

    private final PlaylistMapper playlistMapper;

    public void getMyPlaylists(String userToken, int page, int size) {
    }

    public void getArchive(int page, int size, String sort) {
    }

    public void like(String userToken, int playlistId) {
        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));
        Playlist playlist = getPlaylistById(playlistId);

        if (user.getLikes().contains(playlist)) {
            throw new BadRequestException("playlist with id " + playlistId + " is already liked by " + user.getUsername());
        }

        user.getLikes().add(playlist);

        userRepository.save(user);
        log.info("playlist with id " + playlistId + " is liked by user with username " + user.getUsername());
    }

    public void unlikePlaylist(String userToken, int playlistId) {
        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));
        Playlist playlist = getPlaylistById(playlistId);

        if (!user.getLikes().contains(playlist)) {
            throw new BadRequestException("playlist with id " + playlistId + " is not liked by user with username " + user.getUsername());
        }

        user.getLikes().remove(playlist);
        userRepository.save(user);

        log.info("playlist with id " + playlistId + " is unliked by user with username " + user.getUsername());
    }

    public void getUserPlaylists(String requesterToken, int userId, int page, int size) {
    }

    private User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    private Playlist getPlaylistById(int playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + playlistId + " not found"));
    }
}
