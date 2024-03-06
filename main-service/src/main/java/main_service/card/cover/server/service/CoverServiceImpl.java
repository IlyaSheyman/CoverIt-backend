package main_service.card.cover.server.service;

import lombok.RequiredArgsConstructor;
import main_service.card.cover.client.CoverClient;
import main_service.card.cover.entity.Cover;
import main_service.card.cover.storage.CoverRepository;
import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.card.playlist.dto.PlaylistSmallDto;
import main_service.card.playlist.entity.Playlist;
import main_service.card.playlist.storage.PlaylistRepository;
import main_service.card.track.entity.Track;
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
    @Override
    public PlaylistNewDto getCover(int userId, UrlDto urlDto, Constants.Vibe vibe, Boolean isAbstract) {
        User user = getUserById(userId);
        String url = urlDto.getLink();

        if (user.getIsAuthenticated()) {
            Playlist playlist = playlistRepository.getByUrl(url);

            if (playlist != null) {
                if (playlist.getIsSaved()) {
                    throw new BadRequestException(String.format("playlist with url %s is already covered", url));
                }
                // достаем плейлист
                // добавляем число генераций +1 (если больше трех, то отмена)
            } else {
                PlaylistSmallDto dto = client.getPlaylistDto();
                String coverUrl = client.createCover(urlDto, vibe, isAbstract);
                Cover cover = Cover.builder()
                        .created(LocalDateTime.now())
                        .isAbstract(isAbstract)
                        .link(url)
                        .build();

                coverRepository.save(cover);

                Playlist newPlaylist = Playlist.builder()
                        .generations(1)
                        .title(dto.getTitle())
                        .author(user)
                        .cover()
                        .isSaved(false)
                        .tracks(getTracksFromDto(dto))
                        .build();
            }
        } else {

        }

        //сценарий 1: авторизованный пользователь
            // 1) если private
                // - создается карточка плейлиста, добавляется в бд, playlist.isPrivate = true (не отображается в рекомендациях)

            // 2) если public
                // - создается карточка плейлиста, добавляется в бд, playlist.isPrivate = false (отображается в рекомендациях)

        //сценарий 2: неавторизованный пользователь
            // отсутствует поле private/public
            // создается карточка плейлиста, не добавляется в бд, информация не сохраняется
            // нажатие кнопки "save" -> транзакционный метод регистрация + сохранение 2 в одном - придумать как реализовать

        return null;
    }

    private ArrayList<Track> getTracksFromDto(PlaylistSmallDto dto) {
    }

    public void getCoverByVibeAndUrl(Constants.Vibe vibe, String url) {
        switch (vibe) {
            case DANCING_FLOOR:
                break;
            case NATURE_DOES_NOT_CARE:
                break;
            case SOUND_OF_NOTHING:
                break;
            case CAMPFIRE_CALMNESS:
                break;
            case TOUGH_AND_STRAIGHT:
                break;
            case GARDEN_OF_NOSTALGIA:
                break;
            case FUTURE_IS_NOW:
                break;
            case ROUTINE_SOUNDS:
                break;
            default:

        }
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("user with id %d not found", userId)));
    }

    @Override
    public void savePlaylist(int playlistId, UrlDto imageUrl, Boolean isPrivate) {

    }
}
