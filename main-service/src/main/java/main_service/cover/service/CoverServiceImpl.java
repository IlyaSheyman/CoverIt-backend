package main_service.cover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.cover.client.CoverClient;
import main_service.cover.entity.Cover;
import main_service.cover.storage.CoverRepository;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    public ReleaseNewDto createReleaseCover(String userToken, ReleaseRequest request) {
        User user = extractUser(userToken);

        releaseGenerationsUpdate(request, user);

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

    private void releaseGenerationsUpdate(ReleaseRequest request, User user) {
        if (!user.isSubscribed()) {
            updateGenerationsForNonSubscribedUser(request, user);
        } else {
            updateGenerationsForSubscribedUser(request, user);
        }
    }

    private void updateGenerationsForNonSubscribedUser(ReleaseRequest request, User user) {
        int loFiGenerations = user.getLoFiReleaseGenerations();
        int hiFiGenerations = user.getHiFiReleaseGenerations();

        if (request.getIsLoFi() && (loFiGenerations < LOFI_LIMIT_RELEASE)) {
            user.setLoFiReleaseGenerations(loFiGenerations + 1);
        } else if (!request.getIsLoFi() && (hiFiGenerations < HIFI_LIMIT_RELEASE)) {
            user.setHiFiReleaseGenerations(hiFiGenerations + 1);
        } else {
            int hiFiLeft = HIFI_LIMIT_RELEASE - user.getHiFiReleaseGenerations();
            int loFiLeft = LOFI_LIMIT_RELEASE - user.getLoFiReleaseGenerations();
            log.debug("User with id " + user.getId() + " has reached generations limit.");
            throw new ConflictRequestException("You have reached generations limit. " +
                    "Hi-Fi left: " + hiFiLeft + ". Lo-Fi left: " + loFiLeft);
        }
        userRepository.save(user);
    }

    private void updateGenerationsForSubscribedUser(ReleaseRequest request, User user) {
        int totalGenerations = getTotalGenerations(user);

        if (totalGenerations < SUBSCRIPTION_GENERATIONS_LIMIT) {
            if (request.getIsLoFi()) {
                user.setLoFiReleaseGenerations(user.getLoFiReleaseGenerations() + 1);
            } else {
                user.setHiFiReleaseGenerations(user.getHiFiReleaseGenerations() + 1);
            }
            userRepository.save(user);
        } else {
            throw new ConflictRequestException("Subscriber with id " + user.getId()
                    + " has reached generations limit.");
        }
    }

    @Override
    public PlaylistNewDto createPlaylistCover(String userToken,
                                              UrlDto urlDto,
                                              Vibe vibe,
                                              Boolean isAbstract,
                                              Boolean isLoFi) {
        String url = urlDto.getLink();
        validateAlreadySaved(url);

        PlaylistDto dto = client.getPlaylist(url);

        if (dto == null) {
            throw new RuntimeException("Incorrect response from image generator");
        }
        Playlist newPlaylist = Playlist.builder()
                .createdAt(LocalDateTime.now())
                .url(url)
                .title(dto.getTitle())
                .vibe(vibe)
                .isSaved(false)
                .build();

        ArrayList<Track> tracks = getTracksFromDto(dto);
        newPlaylist.setTracks(tracks);

        if (userToken != null) {
            User user = extractUser(userToken);

            setCounter(isLoFi, user);
            newPlaylist.setAuthor(user);

        }

        if (newPlaylist.getAuthor() == null || !newPlaylist.getAuthor().isSubscribed()) {
            setGenerationsLeft(isLoFi, newPlaylist);
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

    private void setGenerationsLeft(Boolean isLoFi, Playlist newPlaylist) {
        newPlaylist.setLoFiGenerationsLeft(LOFI_LIMIT_PLAYLIST);
        newPlaylist.setHiFiGenerationsLeft(HIFI_LIMIT_PLAYLIST);

        if (isLoFi) {
            newPlaylist.setLoFiGenerationsLeft(newPlaylist.getLoFiGenerationsLeft() - 1);
        } else {
            newPlaylist.setHiFiGenerationsLeft(newPlaylist.getHiFiGenerationsLeft() - 1);
        }
    }

    @Override
    public PlaylistUpdateDto updatePlaylistCover(Vibe vibe,
                                                 Boolean isAbstract,
                                                 int playlistId,
                                                 String userToken,
                                                 Boolean isLoFi) {
        Playlist playlist = getPlaylistById(playlistId);
        User author = playlist.getAuthor();
        UrlDto urlDto = UrlDto.builder()
                .link(playlist.getUrl())
                .build();

        if (author != null && author.isSubscribed()) {
            User user = extractUser(userToken);

            if (user.getId() != author.getId()) {
                throw new ConflictRequestException("Only author of playlist can change its cover");
            }

            return updateForSubscribed(author, isLoFi, playlist, urlDto, vibe, isAbstract);

        } else {
            int hiFiLeft = playlist.getHiFiGenerationsLeft();
            int loFiLeft = playlist.getLoFiGenerationsLeft();

            if (author != null) {
                setCounter(isLoFi, author);
                playlist.setAuthor(author);
            }
            return updateForNonSubscribedOrNonAuth(playlist, urlDto, loFiLeft, hiFiLeft, vibe, isLoFi, isAbstract);
        }
    }

    private PlaylistUpdateDto updateForSubscribed(User author,
                                                  Boolean isLoFi,
                                                  Playlist playlist,
                                                  UrlDto urlDto,
                                                  Vibe vibe,
                                                  Boolean isAbstract) {
        setCounter(isLoFi, author);
        playlist.setAuthor(author);

        int totalGenerations = getTotalGenerations(author);

        if (totalGenerations < SUBSCRIPTION_GENERATIONS_LIMIT) {
            return getPlaylistUpdateDto(vibe, isAbstract, isLoFi, playlist, urlDto);
        } else {
            throw new ConflictRequestException("Subscriber with id " + author.getId()
                    + " has reached generations limit.");
        }
    }

    private PlaylistUpdateDto updateForNonSubscribedOrNonAuth(Playlist playlist,
                                                              UrlDto urlDto,
                                                              Integer loFiLeft,
                                                              Integer hiFiLeft,
                                                              Vibe vibe,
                                                              Boolean isLoFi,
                                                              Boolean isAbstract) {
        if (isLoFi) {
            if (loFiLeft > 0) {
                playlist.setLoFiGenerationsLeft(loFiLeft - 1);
                return getPlaylistUpdateDto(vibe, isAbstract, true, playlist, urlDto);
            } else {
                throw new ConflictRequestException("You have reached generations limit. " +
                        "Hi-Fi left: " + hiFiLeft + ". Lo-Fi left: " + loFiLeft);
            }
        } else {
            if (hiFiLeft > 0) {
                playlist.setHiFiGenerationsLeft(hiFiLeft - 1);
                return getPlaylistUpdateDto(vibe, isAbstract, false, playlist, urlDto);
            } else {
                throw new ConflictRequestException("You have reached generations limit. " +
                        "Hi-Fi left: " + hiFiLeft + ". Lo-Fi left: " + loFiLeft);
            }
        }
    }

    private PlaylistUpdateDto getPlaylistUpdateDto(Vibe vibe,
                                                   Boolean isAbstract,
                                                   Boolean isLofi,
                                                   Playlist playlist,
                                                   UrlDto urlDto) {
        CoverResponse response = client.createPlaylistCover(urlDto, vibe, isAbstract, isLofi);

        Cover cover = Cover.builder()
                .created(LocalDateTime.now())
                .isAbstract(isAbstract)
                .link(response.getUrl())
                .prompt(response.getPrompt())
                .isLoFi(isLofi)
                .build();

        coverRepository.save(cover);
        playlist.setVibe(vibe);
        playlist.setCover(cover);

        return playlistMapper.toPlaylistUpdateDto(playlistRepository.save(playlist));
    }

    @Override
    public PlaylistSaveDto savePlaylist(int playlistId, int coverId, Boolean isPrivate, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        User author = playlist.getAuthor();

        User user = extractUser(userToken);

        if (user.getId() != author.getId()) {
            throw new ConflictRequestException("Only author of playlist can save it");
        }
        playlist.setCover(getCoverById(coverId));
        playlist.setIsPrivate(isPrivate);
        playlist.setIsSaved(true);

        if (playlist.getSavedAt() == null) {
            playlist.setSavedAt(LocalDateTime.now());
        }

        playlistRepository.save(playlist);

        return playlistMapper.toPlaylistSaveDto(playlist);
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

    @Override
    public List<String> getMusicData(String dataType) {
        List<String> data = new ArrayList<>();
        Random random = new Random();

        if ("moods".equals(dataType)) {
            Mood[] allMoods = Mood.values();
            for (int i = 0; i < 8; i++) {
                int randomIndex = random.nextInt(allMoods.length);
                String moodAdjective = allMoods[randomIndex].toString().toLowerCase();
                data.add(moodAdjective);
            }
        } else if ("styles".equals(dataType)) {
            Style[] allStyles = Style.values();
            for (int i = 0; i < 8; i++) {
                int randomIndex = random.nextInt(allStyles.length);
                String styleName = allStyles[randomIndex].toString().toLowerCase();
                data.add(styleName);
            }
        }

        Collections.shuffle(data);
        return data;
    }

    private User extractUser(String userToken) {
        userToken = userToken.substring(7);
        return getUserById(jwtService.extractUserId(userToken));
    }

    private User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }


    private int getTotalGenerations(User user) {
        return user.getLoFiReleaseGenerations()
                + user.getHiFiReleaseGenerations()
                + user.getLoFiPlaylistGenerations()
                + user.getHiFiPlaylistGenerations();
    }

    private void setCounter(Boolean isLoFi, User user) {
        if (isLoFi) {
            int lofi = user.getLoFiPlaylistGenerations() + 1;
            user.setLoFiPlaylistGenerations(lofi);
        } else {
            int hifi = user.getHiFiPlaylistGenerations() + 1;
            user.setHiFiPlaylistGenerations(hifi);
        }
        userRepository.save(user);
    }

    private Playlist getPlaylistById(int playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + playlistId + " not found"));
    }

    private Cover getCoverById(int coverId) {
        return coverRepository.findById(coverId)
                .orElseThrow(() -> new NotFoundException("Cover with id " + coverId + " not found"));
    }

    private void validateAlreadySaved(String url) {
        if (playlistRepository.existsByUrl(url)) {
            throw new BadRequestException(String.format("Playlist %s is already covered", url));
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional //TODO test on server
    protected void deleteUnusedCovers() {
        LocalDateTime expiration = LocalDateTime.now().minusWeeks(SHELF_LIFE);
        List<Release> expiredReleases = releaseRepository.findAllByCreatedAtBefore(expiration);
        List<Playlist> expiredPlaylists = playlistRepository.findAllByIsSavedFalseAndCreatedAtBefore(expiration);

        if (expiredReleases != null && !expiredReleases.isEmpty()) {
            for (Release release : expiredReleases) {
                String coverUrl = release.getCover().getLink();
                UrlDto dto = UrlDto.builder().link(coverUrl).build();
                deleteCover(dto);
            }
        }

        if (expiredPlaylists != null && !expiredPlaylists.isEmpty()) {
            for (Playlist playlist : expiredPlaylists) {
                String coverUrl = playlist.getCover().getLink();
                UrlDto dto = UrlDto.builder().link(coverUrl).build();
                deleteCover(dto);
            }
        }

        log.info("unused covers were deleted successfully");
    }

    @Override
    public void deleteCover(UrlDto url) {
        Cover cover = coverRepository.findByLink(url.getLink());
        coverRepository.delete(cover);
        client.deleteCover(url);
    }
}