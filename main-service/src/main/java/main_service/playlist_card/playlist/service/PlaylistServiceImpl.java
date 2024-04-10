package main_service.playlist_card.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.playlist.dto.PlaylistArchiveDto;
import main_service.playlist_card.playlist.dto.PlaylistMyCollectionDto;
import main_service.playlist_card.playlist.dto.PlaylistUserCollectionDto;
import main_service.playlist_card.playlist.entity.Playlist;
import main_service.playlist_card.playlist.mapper.PlaylistMapper;
import main_service.playlist_card.playlist.storage.PlaylistRepository;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistServiceImpl {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final JwtService jwtService;

    private final PlaylistMapper playlistMapper;

    public List<PlaylistMyCollectionDto> getMyPlaylists(String userToken,
                                                        int page,
                                                        int size,
                                                        Constants.Filters filters) {
        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));
        List<Playlist> liked = user.getLikes();

        List<Playlist> collection = playlistRepository
                .findByAuthor(user, PageRequest.of(page, size))
                .stream()
                .filter(playlist -> playlist.getIsSaved().equals(true))
                .toList();

        if (filters != null) {
            collection = sort(filters, collection);
        }

        List<PlaylistMyCollectionDto> collectionDto = new ArrayList<>();

        for (Playlist playlist : collection) {
            PlaylistMyCollectionDto dto = playlistMapper.toPlaylistMyCollectionDto(playlist);

            if (liked.contains(playlist)) {
                dto.setIsLiked(true);
            } else {
                dto.setIsLiked(false);
            }

            collectionDto.add(dto);
        }

        return collectionDto;
    }

    public List<PlaylistArchiveDto> getArchive(int page,
                                               int size,
                                               Constants.Filters filters,
                                               String userToken) {
        List<Playlist> archive = playlistRepository
                .findAll(PageRequest.of(page, size))
                .stream()
                .filter(playlist -> playlist.getIsSaved().equals(true))
                .filter(playlist -> playlist.getIsPrivate().equals(false))
                .toList();

        if (filters != null) {
            archive = sort(filters, archive);
        }

        if (userToken != null) {
            List<PlaylistArchiveDto> collectionDto = new ArrayList<>();
            String requesterToken = userToken.substring(7);
            User requester = getUserById(jwtService.extractUserId(requesterToken));
            List<Playlist> likedByRequester = requester.getLikes();

            for (Playlist playlist : archive) {
                PlaylistArchiveDto dto = playlistMapper.toPlaylistArchiveDto(playlist);

                if (likedByRequester.contains(playlist)) {
                    dto.setIsLiked(true);
                } else {
                    dto.setIsLiked(false);
                }

                collectionDto.add(dto);
            }

            return collectionDto;
        } else {
            return archive.stream()
                    .map(playlistMapper::toPlaylistArchiveDto)
                    .collect(Collectors.toList());
        }
    }

    public void like(String userToken, int playlistId) {
        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));
        Playlist playlist = getPlaylistById(playlistId);

        if (user.getLikes().contains(playlist)) {
            throw new BadRequestException("playlist with id " + playlistId + " is already liked by " + user.getUsername());
        } else if (playlist.getAuthor().getId() == user.getId()) {
            throw new BadRequestException("user cannot like his own playlist");
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

    public List<PlaylistUserCollectionDto> getUserPlaylists(String userToken,
                                                            int userId,
                                                            int page,
                                                            int size,
                                                            Constants.Filters filters) {
        String requesterToken = userToken.substring(7);
        User requester = getUserById(jwtService.extractUserId(requesterToken));
        User user = getUserById(userId);

        List<Playlist> likedByRequester = requester.getLikes();

        List<Playlist> userCollection = playlistRepository
                .findByAuthor(user, PageRequest.of(page, size))
                .stream()
                .filter(playlist -> playlist.getIsSaved().equals(true))
                .filter(playlist -> playlist.getIsPrivate().equals(false))
                .toList();

        if (filters != null) {
            userCollection = sort(filters, userCollection);
        }

        List<PlaylistUserCollectionDto> collectionDto = new ArrayList<>();

        for (Playlist playlist : userCollection) {
            PlaylistUserCollectionDto dto = playlistMapper.toPlaylistUserCollectionDto(playlist);

            if (likedByRequester.contains(playlist)) {
                dto.setIsLiked(true);
            } else {
                dto.setIsLiked(false);
            }

            collectionDto.add(dto);
        }

        return collectionDto;
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

    private List<Playlist> sort(Constants.Filters filter, List<Playlist> archive) {
        return archive.stream()
                .filter(playlist -> {
                    if (filter.equals(Constants.Filters.ABSTRACT)) {
                        return playlist.getCover().getIsAbstract();
                    } else if (filter.equals(Constants.Filters.NOT_ABSTRACT)) {
                        return !playlist.getCover().getIsAbstract();
                    } else if (filter.equals(Constants.Filters.HI_FI)) {
                        return !playlist.getCover().getIsLoFi();
                    } else if (filter.equals(Constants.Filters.LO_FI)) {
                        return playlist.getCover().getIsLoFi();
                    } else {
                        Constants.Vibe vibe = playlist.getVibe();
                        return vibe != null && vibe.toString().equals(filter.toString());
                    }
                })
                .sorted(Comparator.comparing(Playlist::getSavedAt).reversed())
                .toList();
    }
}