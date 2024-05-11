package main_service.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.cover.entity.Cover;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.NotFoundException;
import main_service.logs.client.TelegramLogsClient;
import main_service.playlist.dto.PlaylistArchiveDto;
import main_service.playlist.dto.PlaylistMyCollectionDto;
import main_service.playlist.dto.PlaylistUserCollectionDto;
import main_service.playlist.entity.Playlist;
import main_service.playlist.mapper.PlaylistMapper;
import main_service.playlist.storage.PlaylistRepository;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
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
    private final TelegramLogsClient logsClient;

    private final PlaylistMapper playlistMapper;

    public List<PlaylistMyCollectionDto> getMyPlaylists(String userToken,
                                                        int page,
                                                        int size,
                                                        Constants.Filters filters) {
        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));
        List<Playlist> liked = user.getLikes();

        List<Playlist> collection = playlistRepository
                .findByAuthor(user)
                .stream()
                .filter(playlist -> playlist.getIsSaved().equals(true))
                .toList();

        if (filters != null) {
            collection = sort(filters, collection);
        }

        List<PlaylistMyCollectionDto> collectionDto = new ArrayList<>();

        for (Playlist playlist : collection) {
            PlaylistMyCollectionDto dto = playlistMapper.toPlaylistMyCollectionDto(playlist);

            dto.setIsLiked(liked.contains(playlist));

            collectionDto.add(dto);
        }

        int start = page * size;
        int end = Math.min((page + 1) * size, collectionDto.size());

        return collectionDto.subList(start, end);
    }

    public List<PlaylistArchiveDto> getArchive(int page,
                                               int size,
                                               Constants.Filters filters,
                                               String userToken) {
        List<Playlist> archive = playlistRepository
                .findAll()
                .stream()
                .filter(playlist -> playlist.getIsSaved().equals(true))
                .filter(playlist -> playlist.getIsPrivate().equals(false))
                .toList();

        if (filters != null) {
            archive = sort(filters, archive);
        }

        List<PlaylistArchiveDto> playlistArchiveDtos;

        if (userToken != null) {
            String requesterToken = userToken.substring(7);
            User requester = getUserById(jwtService.extractUserId(requesterToken));
            List<Playlist> likedByRequester = requester.getLikes();

            playlistArchiveDtos = new ArrayList<>();

            for (Playlist playlist : archive) {
                PlaylistArchiveDto dto = playlistMapper.toPlaylistArchiveDto(playlist);
                dto.setIsLiked(likedByRequester.contains(playlist));
                playlistArchiveDtos.add(dto);
            }
        } else {
            playlistArchiveDtos = archive.stream()
                    .map(playlistMapper::toPlaylistArchiveDto)
                    .collect(Collectors.toList());
        }

        int start = page * size;
        int end = Math.min((page + 1) * size, playlistArchiveDtos.size());

        return playlistArchiveDtos.subList(start, end);
    }

    public void like(String userToken, int playlistId) {
        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));
        Playlist playlist = getPlaylistById(playlistId);

        if (user.getLikes().contains(playlist)) {
            throw new BadRequestException("playlist with id " + playlistId + " is already liked by " + user.getUsername());
        } else if (playlist.getAuthor().getId() == user.getId()) {
            throw new BadRequestException("You can't like your own playlist");
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
            log.debug("Playlist with id " + playlistId + " is not liked by user with username " + user.getUsername());
            throw new BadRequestException("Playlist is not liked by user");
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
                .findByAuthor(user)
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
            dto.setIsLiked(likedByRequester.contains(playlist));

            collectionDto.add(dto);
        }

        int start = page * size;
        int end = Math.min((page + 1) * size, collectionDto.size());

        return collectionDto.subList(start, end);
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
                        return playlist.getCovers().stream().anyMatch(Cover::getIsAbstract);
                    } else if (filter.equals(Constants.Filters.NOT_ABSTRACT)) {
                        return playlist.getCovers().stream().anyMatch(cover -> !cover.getIsAbstract());
                    } else if (filter.equals(Constants.Filters.HI_FI)) {
                        return playlist.getCovers().stream().anyMatch(cover -> !cover.getIsLoFi());
                    } else if (filter.equals(Constants.Filters.LO_FI)) {
                        return playlist.getCovers().stream().anyMatch(Cover::getIsAbstract);
                    } else {
                        Constants.Vibe vibe = playlist.getVibe();
                        return vibe != null && vibe.toString().equals(filter.toString());
                    }
                })
                .sorted(Comparator.comparing(Playlist::getSavedAt).reversed())
                .toList();
    }
}