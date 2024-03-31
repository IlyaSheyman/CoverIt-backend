package main_service.playlist_card.cover.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.playlist_card.cover.client.CoverClient;
import main_service.playlist_card.cover.entity.Cover;
import main_service.playlist_card.cover.storage.CoverRepository;
import main_service.playlist_card.playlist.dto.PlaylistDto;
import main_service.playlist_card.playlist.dto.PlaylistNewDto;
import main_service.playlist_card.playlist.dto.PlaylistSaveDto;
import main_service.playlist_card.playlist.dto.PlaylistUpdateDto;
import main_service.playlist_card.playlist.entity.Playlist;
import main_service.playlist_card.playlist.mapper.PlaylistMapper;
import main_service.playlist_card.playlist.storage.PlaylistRepository;
import main_service.playlist_card.track.dto.TrackDto;
import main_service.playlist_card.track.entity.Track;
import main_service.playlist_card.track.storage.TrackRepository;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.ConflictRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final CoverRepository coverRepository;
    private final TrackRepository trackRepository;

    private final JwtService jwtService;

    private final PlaylistMapper playlistMapper;

    @Override
    public PlaylistNewDto getCover(String userToken, UrlDto urlDto, Constants.Vibe vibe, Boolean isAbstract) {
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

        if (userToken != null) { //TODO проверить, работает ли установление автора
            userToken = userToken.substring(7);
            User user = getUserById(jwtService.extractUserId(userToken));
            newPlaylist.setAuthor(user);
        }

        String coverUrl = client.createCover(urlDto, vibe, isAbstract);

        Cover cover = Cover.builder()
                .created(LocalDateTime.now())
                .isAbstract(isAbstract)
                .link(coverUrl)
                .build();

        coverRepository.save(cover);
        newPlaylist.setCover(cover);

        return playlistMapper.toPlaylistNewDto(playlistRepository.save(newPlaylist));
    }

    @Override
    public PlaylistUpdateDto updateCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        int generations = playlist.getGenerations();

        User author = playlist.getAuthor();

        if (author != null) {
            userToken = userToken.substring(7);
            User user = getUserById(jwtService.extractUserId(userToken));

            if (user.getId() != author.getId()) {
                throw new ConflictRequestException("only author of playlist can change its cover");
            }
        }

        if (generations < 3) {
            UrlDto urlDto = UrlDto.builder()
                    .link(playlist.getUrl())
                    .build();

            String coverUrl = client.createCover(urlDto, vibe, isAbstract);

            Cover cover = Cover.builder()
                    .created(LocalDateTime.now())
                    .isAbstract(isAbstract)
                    .link(coverUrl)
                    .build();

            coverRepository.save(cover);

            generations++;

            playlist.setCover(cover);
            playlist.setGenerations(generations);

            return playlistMapper.toPlaylistUpdateDto(playlistRepository.save(playlist));
        } else {
            throw new BadRequestException("generations limit is already reached");
        }
    }

    @Override
    public void getMyPlaylists(String userToken) {
        //TODO
    }

    @Override
    public PlaylistSaveDto savePlaylist(int playlistId, Boolean isPrivate, String userToken) {
        Playlist playlist = getPlaylistById(playlistId);
        User author = playlist.getAuthor();

        userToken = userToken.substring(7);
        User user = getUserById(jwtService.extractUserId(userToken));

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

}