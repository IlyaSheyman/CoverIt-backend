package main_service.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.cover.client.CoverClient;
import main_service.cover.entity.Cover;
import main_service.cover.service.UrlDto;
import main_service.cover.storage.CoverRepository;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.NotFoundException;
import main_service.logs.service.TelegramLogsService;
import main_service.playlist.dto.*;
import main_service.playlist.entity.Playlist;
import main_service.playlist.mapper.PlaylistMapper;
import main_service.playlist.storage.PlaylistRepository;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistServiceImpl implements PlaylistService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final JwtService jwtService;
    private final TelegramLogsService logsService;
    private final CoverRepository coverRepository;
    private final CoverClient client;

    private final PlaylistMapper playlistMapper;


    //TODO refactor to get only my collection
    @Override
    public List<PlaylistMyCollectionDto> getMyPlaylists(String userToken,
                                                        int page,
                                                        int size,
                                                        Constants.Filters filters) {
        User user = extractUserFromToken(userToken);
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
            dto.setAuthor(playlist.getAuthor().getUsername());

            collectionDto.add(dto);
        }

        int start = page * size;
        int end = Math.min((page + 1) * size, collectionDto.size());

        if (start >= collectionDto.size()) {
            return Collections.emptyList();
        }

        return collectionDto.subList(start, end);
    }

    //TODO refactor to get my playlists
    @Override
    public List<PlaylistMyCollectionDto> getLikedPlaylists(String userToken, int page, int size, Constants.Filters filters) {
        return List.of();
    }

    @Override
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

        List<PlaylistArchiveDto> playlistArchiveDtos = new ArrayList<>();


        if (userToken != null) {
            User requester = extractUserFromToken(userToken);
            List<Playlist> likedByRequester = requester.getLikes();

            for (Playlist playlist : archive) {
                PlaylistArchiveDto dto = playlistMapper.toPlaylistArchiveDto(playlist);
                dto.setIsLiked(likedByRequester.contains(playlist));
                dto.setAuthor(playlist.getAuthor().getUsername());
                playlistArchiveDtos.add(dto);
            }
        } else {
            for (Playlist playlist : archive) {
                PlaylistArchiveDto dto = playlistMapper.toPlaylistArchiveDto(playlist);
                dto.setAuthor(playlist.getAuthor().getUsername());
                playlistArchiveDtos.add(dto);
            }
        }

        int start = page * size;
        int end = Math.min((page + 1) * size, playlistArchiveDtos.size());

        if (start >= playlistArchiveDtos.size()) {
            return Collections.emptyList();
        }

        return playlistArchiveDtos.subList(start, end);
    }

    @Override
    public void like(String userToken, int playlistId) {
        User user = extractUserFromToken(userToken);
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

    @Override
    public void unlikePlaylist(String userToken, int playlistId) {
        User user = extractUserFromToken(userToken);
        Playlist playlist = getPlaylistById(playlistId);

        if (!user.getLikes().contains(playlist)) {
            throw new BadRequestException("Playlist is not liked by user");
        }

        user.getLikes().remove(playlist);
        userRepository.save(user);

        log.info("playlist with id " + playlistId + " is unliked by user with username " + user.getUsername());
    }

    @Override
    public List<PlaylistUserCollectionDto> getUserPlaylists(String userToken,
                                                            int userId,
                                                            int page,
                                                            int size,
                                                            Constants.Filters filters) {
        User requester = extractUserFromToken(userToken);
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

    private User extractUserFromToken(String userToken) {
        userToken = userToken.substring(7);
        return getUserById(jwtService.extractUserId(userToken));
    }

    private User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public PlaylistShareDto getPlaylistSharing(int playlistId) {
        Playlist playlist = getPlaylistById(playlistId);
        if (playlist.getIsSaved() && !playlist.getIsPrivate()) {
            return playlistMapper.toPlaylistGetDto(playlist);
        } else {
            throw new BadRequestException("This playlist is not saved or private");
        }
    }

    @Override
    public PlaylistNewDto getPlaylist(int playlistId, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        if (userToken != null) {
            User author = extractUserFromToken(userToken);
            if (author.getId() != playlist.getAuthor().getId()) {
                throw new BadRequestException("You are not the author of this playlist");
            }
        }
        return playlistMapper.toPlaylistNewDto(playlist);
    }

    @Override
    public void deletePlaylist(String userToken, int playistId) {
        User user = extractUserFromToken(userToken);
        Playlist playlist = getPlaylistById(playistId);
        List<Cover> covers = playlist.getCovers();

        if (playlist.getAuthor().getId() == user.getId()) {
            for (Cover cover : covers) {
                deleteCover(cover);
            }

            playlistRepository.delete(playlist);
        } else {
            throw new BadRequestException("You should be author of playlist to delete it");
        }

        logsService.info("Playlist deleted",
                String.format("User %s deleted playlist with id %d with %d covers",
                        user.getUsername(),
                        playlist.getId(),
                        covers.size()),
                playlistMapper.toPlaylistLogsDto(playlist),
                null
        );
    }

    public void deleteCover(Cover cover) {
        coverRepository.delete(cover);
        client.deleteCover(UrlDto
                .builder()
                .link(cover.getLink())
                .build());
    }

    private Playlist getPlaylistById(int playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + playlistId + " not found"));
    }
}