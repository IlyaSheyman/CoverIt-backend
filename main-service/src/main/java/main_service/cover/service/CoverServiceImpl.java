package main_service.cover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.cover.client.CoverClient;
import main_service.cover.dto.DeletedCacheDto;
import main_service.cover.entity.Cover;
import main_service.cover.entity.ReleaseCover;
import main_service.cover.storage.CoverRepository;
import main_service.cover.storage.ReleaseCoverRepository;
import main_service.exception.dto.LimitExceptionMessage;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.LimitReachedException;
import main_service.exception.model.NotFoundException;
import main_service.kafka.producer.KafkaProducerService;
import main_service.logs.service.TelegramLogsService;
import main_service.playlist.dto.*;
import main_service.playlist.entity.Playlist;
import main_service.playlist.mapper.PlaylistMapper;
import main_service.playlist.storage.PlaylistRepository;
import main_service.playlist.track.dto.TrackDto;
import main_service.playlist.track.entity.Track;
import main_service.playlist.track.storage.TrackRepository;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.dto.ReleaseSaveDto;
import main_service.release.dto.ReleaseUpdateDto;
import main_service.release.entity.Release;
import main_service.release.mapper.ReleaseMapper;
import main_service.release.mapper.ReleaseRequestMapper;
import main_service.release.request.ReleaseRequest;
import main_service.release.storage.ReleaseRepository;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static main_service.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final CoverRepository coverRepository;
    private final ReleaseCoverRepository releaseCoverRepository;
    private final TrackRepository trackRepository;
    private final ReleaseRepository releaseRepository;

    private final JwtService jwtService;

    private final PlaylistMapper playlistMapper;
    private final ReleaseMapper releaseMapper;
    private final ReleaseRequestMapper requestMapper;

    private final TelegramLogsService logsService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public ReleaseNewDto createReleaseCover(String userToken, ReleaseRequest request) {
        User user = extractUser(userToken);

        List<Release> usersReleases = releaseRepository.findAllByAuthor(user);
        if (usersReleases.stream().anyMatch(release -> release.getTitle().equals(request.getTitle()))) {
            throw new ConflictRequestException("You've already covered release with this title");
        }

        releaseGenerationsUpdate(request, user);

        Release newRelease = Release.builder()
                .title(request.getTitle())
                .createdAt(LocalDateTime.now())
                .author(user)
                .build();

        newRelease = releaseRepository.save(newRelease);

        CoverResponse coverResponse = client.createReleaseCover(requestMapper.toReleaseRequestDto(request));

        ReleaseCover newCover = buildAndSaveReleaseCover(request, coverResponse);

        List<ReleaseCover> covers = new ArrayList<>();
        covers.add(newCover);
        newRelease.setCovers(covers);

        newRelease = releaseRepository.save(newRelease);

        logsService.info("New release",
                String.format("User <b>@%s</b> created new release <b>%s</b>", user.getUsername(), newRelease.getTitle()),
                releaseMapper.toReleaseLogsDto(newRelease),
                newCover.getLink());

        String message = String.format("Check cover saved status for cover ID %d in 1 day", newCover.getId());
        kafkaProducerService.sendCheckCoverMessage(message);

        return releaseMapper.toReleaseNewDto(newRelease);
    }

    @Override
    public ReleaseUpdateDto updateReleaseCover(String userToken, int releaseId, ReleaseRequest request) {
        User user = extractUser(userToken);
        Release release = getReleaseById(releaseId);

        if (release.getAuthor().getId() != user.getId()) {
            throw new BadRequestException("You should be author of release to update cover");
        }

        releaseGenerationsUpdate(request, user);

        CoverResponse coverResponse = client.createReleaseCover(requestMapper.toReleaseRequestDto(request));

        ReleaseCover newCover = buildAndSaveReleaseCover(request, coverResponse);
        release.getCovers().add(newCover);

        releaseCoverRepository.save(newCover);
        release = releaseRepository.save(release);

        logsService.info("Update release",
                String.format("User <b>@%s</b> generated new cover for release <b>%s</b>", user.getUsername(), release.getTitle()),
                releaseMapper.toReleaseSaveDto(release),
                newCover.getLink());

        return releaseMapper.toReleaseUpdateDto(release);
    }

    private ReleaseCover buildAndSaveReleaseCover(ReleaseRequest request, CoverResponse coverResponse) {
        ReleaseCover newCover = ReleaseCover.releaseCoverBuilder()
                .surrounding(request.getSurrounding())
                .object(request.getObject())
                .created(LocalDateTime.now())
                .isLoFi(request.getIsLoFi())
                .link(coverResponse.getUrl())
                .prompt(coverResponse.getPrompt())
                .isSaved(false)
                .build();

        newCover = releaseCoverRepository.save(newCover);

        newCover.setMood(request.getMood());
        newCover.setCoverDescription(request.getCoverDescription());

        return releaseCoverRepository.save(newCover);
    }

    @Override
    public ReleaseSaveDto saveRelease(String userToken, int releaseId, int coverId) {
        User user = extractUser(userToken);
        Release release = getReleaseById(releaseId);
        ReleaseCover cover = getReleaseCoverById(coverId);

        if (release.getAuthor().getId() != user.getId()) {
            throw new BadRequestException("You should be author of release to save it");
        } else if (release.isSaved()) {
            throw new ConflictRequestException("This release is already saved");
        } else if (!release.getCovers().contains(cover)) {
            throw new BadRequestException("There is no such a cover for this release");
        }

        cover.setIsSaved(true);
        release.setSaved(true);
        release.setSavedAt(LocalDateTime.now());

        releaseCoverRepository.save(cover);
        release = releaseRepository.save(release);

        logsService.info("Save release",
                String.format("User <b>@%s</b> saved release <b>%s</b> with cover %d (id)",
                        user.getUsername(),
                        release.getTitle(),
                        cover.getId()),
                releaseMapper.toReleaseSaveDto(release),
                cover.getLink());

        return releaseMapper.toReleaseSaveDto(release);
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

            handleGenerationLimitExceeded(hiFiLeft, loFiLeft, "release");
        }
        userRepository.save(user);
    }

    private void handleGenerationLimitExceeded(int hiFiLeft, int loFiLeft, String type) {
        LimitExceptionMessage errorMessage;
        if (type.equals("release")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);

            Duration duration = Duration.between(now, midnight);

            int renewInHours = (int) duration.toHours();
            int renewInMinutes = (int) (duration.toMinutes() % 60);

            errorMessage = LimitExceptionMessage.builder()
                    .hiFiLeft(hiFiLeft)
                    .loFiLeft(loFiLeft)
                    .hoursLeft(renewInHours)
                    .minutesLeft(renewInMinutes)
                    .build();

        } else {
            errorMessage = LimitExceptionMessage.builder()
                    .hiFiLeft(hiFiLeft)
                    .loFiLeft(loFiLeft)
                    .build();
        }

        throw new LimitReachedException(errorMessage);
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

        User user = null;

        if (userToken != null) {
            user = extractUser(userToken);

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
                .vibe(vibe)
                .isSaved(false)
                .prompt(response.getPrompt())
                .link(response.getUrl())
                .build();

        coverRepository.save(cover);

        List<Cover> covers = new ArrayList<>();
        covers.add(cover);
        newPlaylist.setCovers(covers);

        newPlaylist = playlistRepository.save(newPlaylist);

        String logMessage = (user == null) ?
                String.format("New playlist <b>%s</b> by unauthorized user", newPlaylist.getTitle()) :
                String.format("User <b>%s</b> created new playlist <b>%s</b>", user.getUsername(), newPlaylist.getTitle());

        logsService.info("New playlist",
                logMessage,
                playlistMapper.toPlaylistLogsDto(newPlaylist),
                cover.getLink());

        return playlistMapper.toPlaylistNewDto(newPlaylist);
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

        if (userToken != null) {
            User user = extractUser(userToken);

            if (author == null || user.getId() != author.getId()) {
                throw new ConflictRequestException("Only author of playlist can change its cover");
            }
        }

        if (author != null && author.isSubscribed()) {
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
                handleGenerationLimitExceeded(hiFiLeft, loFiLeft, "playlist");
                return null;
            }
        } else {
            if (hiFiLeft > 0) {
                playlist.setHiFiGenerationsLeft(hiFiLeft - 1);
                return getPlaylistUpdateDto(vibe, isAbstract, false, playlist, urlDto);
            } else {
                handleGenerationLimitExceeded(hiFiLeft, loFiLeft, "playlist");
                return null;
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
                .vibe(vibe)
                .isSaved(false)
                .build();

        coverRepository.save(cover);
        playlist.setVibe(vibe);

        List<Cover> covers = playlist.getCovers();
        covers.add(cover);

        playlist = playlistRepository.save(playlist);

        logsService.info("Playlist cover updated",
                String.format("New cover for playlist with id %d", playlist.getId()),
                playlistMapper.toPlaylistLogsDto(playlist),
                cover.getLink());

        return playlistMapper.toPlaylistUpdateDto(playlist);
    }

    @Override
    public PlaylistSaveDto savePlaylist(int playlistId, int coverId, Boolean isPrivate, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        User author = playlist.getAuthor();

        User user = extractUser(userToken);

        if (user.getId() != author.getId()) {
            throw new ConflictRequestException("Only author of playlist can save it");
        } else if (playlist.getIsSaved()) {
            throw new ConflictRequestException("This playlist is already saved");
        }


        Cover chosen = getCoverById(coverId);
        List<Cover> covers = playlist.getCovers();

        if (covers.contains(chosen)) {
            chosen.setIsSaved(true);
            coverRepository.save(chosen);
            playlist.setVibe(chosen.getVibe());
        }

        playlist.setIsPrivate(isPrivate);
        playlist.setIsSaved(true);
        playlist.setSavedAt(LocalDateTime.now());

        playlist = playlistRepository.save(playlist);

        logsService.info("Playlist saved",
                String.format("Playlist with id %d is saved by <b>%s</b>. Chosen cover id: %d",
                        playlist.getId(),
                        user.getUsername(),
                        chosen.getId()),
                playlistMapper.toPlaylistLogsDto(playlist),
                chosen.getLink());

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
        Random random = new Random();

        Set<String> uniqueData = new HashSet<>();

        if ("moods".equals(dataType)) {
            Mood[] allMoods = Mood.values();
            while (uniqueData.size() < 8) {
                int randomIndex = random.nextInt(allMoods.length);
                String moodAdjective = allMoods[randomIndex].toString().toLowerCase().replace("_", " ");
                uniqueData.add(moodAdjective);
            }
        } else if ("styles".equals(dataType)) {
            Style[] allStyles = Style.values();
            while (uniqueData.size() < 8) {
                int randomIndex = random.nextInt(allStyles.length);
                String styleName = allStyles[randomIndex].name().toLowerCase().replace("_", " ");
                uniqueData.add(styleName);
            }
        }

        return new ArrayList<>(uniqueData);
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

    private ReleaseCover getReleaseCoverById(int coverId) {
        return releaseCoverRepository.findById(coverId)
                .orElseThrow(() -> new NotFoundException("Release cover with id " + coverId + " not found"));
    }
    private Cover getCoverById(int coverId) {
        return coverRepository.findById(coverId)
                .orElseThrow(() -> new NotFoundException("Cover with id " + coverId + " not found"));
    }

    private Release getReleaseById(int releaseId) {
        return releaseRepository.findById(releaseId)
                .orElseThrow(() -> new NotFoundException("Release with id " + releaseId + " not found"));
    }

    private void validateAlreadySaved(String url) {
        if (playlistRepository.existsByUrlAndIsSavedTrue(url)) {
            throw new BadRequestException("Playlist is already covered");
        }
    }

    @Override
    @Transactional
    @Async
    public void deleteCache(String userToken, String password) {
        User user = extractUser(userToken);
        if (user.getEmail().equals(ADMIN_EMAIL)) {
            
        }
        LocalDateTime expiration = LocalDateTime.now().minusDays(SHELF_LIFE);

        // Find all expired releases, playlists, and covers
        List<Release> expiredReleases = releaseRepository.findAllBySavedFalseAndCreatedAtBefore(expiration);
        List<Playlist> expiredPlaylists = playlistRepository.findAllByIsSavedFalseAndCreatedAtBefore(expiration);
        List<Cover> expiredCovers = coverRepository.findAllByIsSavedFalseAndCreatedBefore(expiration);

        // Collect IDs and titles of deleted entities for logging
        List<Integer> deletedCovers = new ArrayList<>();
        HashMap<Integer, String> deletedReleases = new HashMap<>();
        HashMap<Integer, String> deletedPlaylists = new HashMap<>();

        // Process expired releases
        for (Release release : expiredReleases) {
            deleteRelease(release, deletedCovers, deletedReleases);
        }

        // Process expired playlists
        for (Playlist playlist : expiredPlaylists) {
            deletePlaylist(playlist, deletedCovers, deletedPlaylists);
        }

        // Process expired covers
        for (Cover cover : expiredCovers) {
            deleteCoverAndDetachFromRelatedEntities(cover, deletedCovers);
        }

        // Create DTO for logging
        DeletedCacheDto dto = DeletedCacheDto.builder()
                .covers(deletedCovers)
                .releases(deletedReleases)
                .playlists(deletedPlaylists)
                .build();

        // Log deletion details
        logsService.info("Unused covers deleted",
                "Cache deleted automatically",
                dto,
                null);
    }

    private void deleteRelease(Release release, List<Integer> deletedCovers, Map<Integer, String> deletedReleases) {
        List<Cover> covers = new ArrayList<>(release.getCovers());

        // Clear covers from release
        release.getCovers().clear();
        releaseRepository.save(release);

        // Delete each cover and collect IDs
        for (Cover cover : covers) {
            deleteCover(cover);
            deletedCovers.add(cover.getId());
        }

        // Delete the release itself
        releaseRepository.delete(release);
        deletedReleases.put(release.getId(), release.getTitle());
    }

    private void deletePlaylist(Playlist playlist, List<Integer> deletedCovers, Map<Integer, String> deletedPlaylists) {
        List<Cover> covers = new ArrayList<>(playlist.getCovers());
        // Clear covers from playlist
        playlist.getCovers().clear();
        playlistRepository.save(playlist);

        // Delete each cover and collect IDs
        for (Cover cover : covers) {
            deleteCover(cover);
            deletedCovers.add(cover.getId());
        }

        // Delete the playlist itself
        playlistRepository.delete(playlist);
        deletedPlaylists.put(playlist.getId(), playlist.getTitle());
    }

    private void deleteCoverAndDetachFromRelatedEntities(Cover cover, List<Integer> deletedCovers) {
        // Find and detach cover from related entities (Release or Playlist)
        Playlist playlistSource = playlistRepository.findByCoversContains(cover);
        Release releaseSource = releaseRepository.findByCoversContains(cover);

        if (playlistSource != null) {
            playlistSource.getCovers().remove(cover);
            playlistRepository.save(playlistSource);
        } else if (releaseSource != null) {
            releaseSource.getCovers().remove(cover);
            releaseRepository.save(releaseSource);
        }

        // Delete the cover itself
        deleteCover(cover);
        deletedCovers.add(cover.getId());
    }

    @Override
    public void deleteCover(Cover cover) {
        coverRepository.delete(cover);
        client.deleteCover(UrlDto.builder().link(cover.getLink()).build());
    }

}