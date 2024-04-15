package main_service.cover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.cover.storage.CoverRepository;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
import main_service.cover.client.CoverClient;
import main_service.cover.entity.Cover;
import main_service.playlist.dto.*;
import main_service.playlist.entity.Playlist;
import main_service.playlist.mapper.PlaylistMapper;
import main_service.playlist.storage.PlaylistRepository;
import main_service.playlist.track.dto.TrackDto;
import main_service.playlist.track.entity.Track;
import main_service.playlist.track.storage.TrackRepository;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.entity.Release;
import main_service.release.mapper.ReleaseMapper;
import main_service.release.mapper.ReleaseRequestMapper;
import main_service.release.request.ReleaseRequest;
import main_service.release.storage.ReleaseRepository;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static main_service.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final CoverRepository coverRepository;
    private final TrackRepository trackRepository;
    private final ReleaseRepository releaseRepository;

    private final JwtService jwtService;

    private final PlaylistMapper playlistMapper;
    private final ReleaseMapper releaseMapper;
    private final ReleaseRequestMapper requestMapper;

    @Override
    public ReleaseNewDto getReleaseCover(String userToken, ReleaseRequest request) {
        User user = extractUser(userToken);

        generationsUpdate(request, user);

        CoverResponse coverResponse = client.createReleaseCover(requestMapper.toReleaseRequestDto(request));

        Cover newCover = Cover.builder()
                .created(LocalDateTime.now())
                .isLoFi(request.getIsLoFi())
                .link(coverResponse.getUrl())
                .prompt(coverResponse.getPrompt())
                .build();

        Release newRelease = Release.builder()
                .title(request.getTitle())
                .createdAt(LocalDateTime.now())
                .cover(newCover)
                .author(user)
                .build();

        coverRepository.save(newCover);
        releaseRepository.save(newRelease);

        return releaseMapper.toReleaseNewDto(newRelease);
    }

    private void generationsUpdate(ReleaseRequest request, User user) {
        if (!user.isSubscribed()) {
            updateGenerationsForNonSubscribedUser(request, user);
        } else {
            updateGenerationsForSubscribedUser(request, user);
        }
    }

    private void updateGenerationsForNonSubscribedUser(ReleaseRequest request, User user) {
        int loFiGenerations = user.getLoFiGenerations();
        int hiFiGenerations = user.getHiFiGenerations();

        if (request.getIsLoFi() && (loFiGenerations < LOFI_LIMIT)) {
            user.setLoFiGenerations(loFiGenerations + 1);
        } else if (!request.getIsLoFi() && (hiFiGenerations < HIFI_LIMIT)) {
            user.setHiFiGenerations(hiFiGenerations + 1);
        } else {
            throw new ConflictRequestException("User with id " + user.getId() + " has reached generations limit");
        }
        userRepository.save(user);
    }

    private void updateGenerationsForSubscribedUser(ReleaseRequest request, User user) {
        int totalGenerations = user.getLoFiGenerations() + user.getHiFiGenerations() + user.getPlaylistGenerations();

        if (totalGenerations < SUBSCRIPTION_GENERATIONS_LIMIT) {
            if (request.getIsLoFi()) {
                user.setLoFiGenerations(user.getLoFiGenerations() + 1);
            } else {
                user.setHiFiGenerations(user.getHiFiGenerations() + 1);
            }
            userRepository.save(user);
        } else {
            throw new ConflictRequestException("User with id " + user.getId() + " has reached generations limit");
        }
    }

    @Override
    public PlaylistNewDto createPlaylistCover(String userToken,
                                              UrlDto urlDto,
                                              Constants.Vibe vibe,
                                              Boolean isAbstract,
                                              Boolean isLoFi) {
        String url = urlDto.getLink();
        validateAlreadySaved(url);

        PlaylistDto dto = client.getPlaylist(url);

        Playlist newPlaylist = Playlist.builder()
                .generations(1)
                .url(url)
                .title(dto.getTitle())
                .vibe(vibe)
                .isSaved(false)
                .build();

        ArrayList<Track> tracks = getTracksFromDto(dto);
        newPlaylist.setTracks(tracks);

        if (userToken != null) {
            User user = extractUser(userToken);
            int generations = user.getPlaylistGenerations();
            user.setPlaylistGenerations(generations + 1);

            newPlaylist.setAuthor(userRepository.save(user));
        }

        CoverResponse response = client.createPlaylistCover(urlDto, vibe, isAbstract, isLoFi);

        Cover cover = Cover.builder()
                .created(LocalDateTime.now())
                .isAbstract(isAbstract)
                .isLoFi(isLoFi)
                .prompt(response.getPrompt())
                .link(response.getUrl())
                .build();

        coverRepository.save(cover);
        newPlaylist.setCover(cover);

        return playlistMapper.toPlaylistNewDto(playlistRepository.save(newPlaylist));
    }

    @Override
    public PlaylistUpdateDto updatePlaylistCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        int generations = playlist.getGenerations();
        Cover previousCover = playlist.getCover();

        User author = playlist.getAuthor();

        UrlDto urlDto = UrlDto.builder()
                .link(playlist.getUrl())
                .build();

        if (author != null) {
            User user = extractUser(userToken);

            if (user.getId() != author.getId()) {
                throw new ConflictRequestException("only author of playlist can change its cover");
            }

            int playlistGenerations = user.getPlaylistGenerations();
            user.setPlaylistGenerations(playlistGenerations + 1);

            userRepository.save(user);

            if (user.isSubscribed()) {
                int totalGenerations = user.getLoFiGenerations()
                        + user.getHiFiGenerations()
                        + user.getPlaylistGenerations();

                if (totalGenerations  < SUBSCRIPTION_GENERATIONS_LIMIT) {
                    return getPlaylistUpdateDto(vibe, isAbstract, playlist, generations, urlDto);
                } else {
                    throw new ConflictRequestException("User with id " + user.getId()
                            + " has reached generations limit");
                }
            }
        }

        if (previousCover.getIsLoFi()) { // it is not possible to regenerate the hi-fi cover
            if (generations < 3) {
                return getPlaylistUpdateDto(vibe, isAbstract, playlist, generations, urlDto);
            } else {
                throw new BadRequestException("generations limit is already reached");
            }
        } else {
            throw new BadRequestException("limit for hi-fi covers = 1");
        }
    }

    private PlaylistUpdateDto getPlaylistUpdateDto(Vibe vibe, Boolean isAbstract, Playlist playlist, int generations, UrlDto urlDto) {
        CoverResponse response = client.createPlaylistCover(urlDto, vibe, isAbstract, true);

        Cover cover = Cover.builder()
                .created(LocalDateTime.now())
                .isAbstract(isAbstract)
                .link(response.getUrl())
                .prompt(response.getPrompt())
                .isLoFi(true)
                .build();

        coverRepository.save(cover);

        generations++;

        playlist.setCover(cover);
        playlist.setGenerations(generations);

        return playlistMapper.toPlaylistUpdateDto(playlistRepository.save(playlist));
    }

    @Override
    public PlaylistSaveDto savePlaylist(int playlistId, Boolean isPrivate, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        User author = playlist.getAuthor();

        User user = extractUser(userToken);

        if (user.getId() != author.getId()) {
            throw new ConflictRequestException("only author of playlist can save it");
        }

        playlist.setIsPrivate(isPrivate);
        playlist.setIsSaved(true);

        if (playlist.getSavedAt() == null) {
            playlist.setSavedAt(LocalDateTime.now());
        }

        playlistRepository.save(playlist);

        return playlistMapper.toPlaylistSaveDto(playlist);
    }

    private Playlist getPlaylistById(int playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + playlistId + " not found"));
    }

    private void validateAlreadySaved(String url) {
        if (playlistRepository.existsByUrl(url)) {
            throw new BadRequestException(String.format("playlist with url %s is already covered", url));
        }
    }

    private ArrayList<Track> getTracksFromDto(PlaylistDto playlistDto) {
        ArrayList<Track> tracks = new ArrayList<>();

        for (TrackDto dto : playlistDto.getTracks()) {
            String authorsString = String.join(", ", dto.getAuthors());
            String title = dto.getTitle();
            Track track = trackRepository.findByTitleAndAuthors(title, authorsString);

            if (track == null) {
                Track newTrack = Track.builder()
                        .authors(authorsString)
                        .title(title)
                        .build();

                trackRepository.save(newTrack);
                tracks.add(newTrack);
            } else {
                tracks.add(track);
            }
        }
        return tracks;
    }

    private User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    private User extractUser(String userToken) {
        userToken = userToken.substring(7);
        return getUserById(jwtService.extractUserId(userToken));
    }
}