package main_service.card.cover.server.service;

import lombok.RequiredArgsConstructor;
import main_service.card.cover.client.CoverClient;
import main_service.card.cover.entity.Cover;
import main_service.card.cover.storage.CoverRepository;
import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.card.playlist.dto.PlaylistSmallDto;
import main_service.card.playlist.dto.PlaylistUpdateDto;
import main_service.card.playlist.entity.Playlist;
import main_service.card.playlist.mapper.PlaylistMapper;
import main_service.card.playlist.storage.PlaylistRepository;
import main_service.card.track.entity.Track;
import main_service.config.security.JwtService;
import main_service.constants.Constants;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.NotFoundException;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final CoverRepository coverRepository;
    private final JwtService jwtService;

    private final PlaylistMapper playlistMapper;

    @Override
    public PlaylistNewDto getCover(String userToken, UrlDto urlDto, Constants.Vibe vibe, Boolean isAbstract) {
        String url = urlDto.getLink();
        validateAlreadySaved(url);

        PlaylistSmallDto dto = client.getPlaylist(url);

        Playlist newPlaylist = Playlist.builder()
                .generations(1)
                .title(dto.getTitle())
                .isSaved(false)
                .tracks(getTracksFromDto(dto))
                .build();

        if (userToken != null) {
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
    public PlaylistUpdateDto updateCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId) {
        Playlist playlist = getPlaylistById(playlistId);

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

        int generations = playlist.getGenerations() + 1;

        playlist.setCover(cover);
        playlist.setGenerations(generations);

        return playlistMapper.toPlaylistUpdateDto(playlistRepository.save(playlist));
        //TODO добавить во фронте всплывающее окно с возможностью выбора вайба и абстрактности при регенереции
    }

    private Playlist getPlaylistById(int playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + playlistId + " not found"));
    }

    private void validateAlreadySaved(String url) {
        Playlist playlist = playlistRepository.getByUrl(url);

        if (playlist != null) {
            if (playlist.getIsSaved()) {
                throw new BadRequestException(String.format("playlist with url %s is already covered", url));
            }
        }
    }

    private ArrayList<Track> getTracksFromDto(PlaylistSmallDto dto) {
        return null;
    }

    private User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public void savePlaylist(int playlistId, Boolean isPrivate, String userToken) {
        //сценарий 1: авторизованный пользователь
        // 1) если private
        // - создается карточка плейлиста, добавляется в бд, playlist.isPrivate = true (не отображается в рекомендациях)

        // 2) если public
        // - создается карточка плейлиста, добавляется в бд, playlist.isPrivate = false (отображается в рекомендациях)

        //сценарий 2: неавторизованный пользователь
        // отсутствует поле private/public
        // создается карточка плейлиста, не добавляется в бд, информация не сохраняется
        // нажатие кнопки "save" -> транзакционный метод регистрация + сохранение 2 в одном - придумать как реализовать

    }
}
