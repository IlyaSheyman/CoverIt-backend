package main_service.card.cover.server.service;

import lombok.RequiredArgsConstructor;
import main_service.card.cover.client.CoverClient;
import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.constants.Constants;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;
    private final UserRepository userRepository;

    @Override
    public PlaylistNewDto getCover(int userId, String url, Constants.Vibe vibe, Boolean isAbstract) {
        if (isAuthenticated(userId)) {

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

    private boolean isAuthenticated(int userId) {
        return false;
    }

    @Override
    public void savePlaylist(int playlistId, UrlDto imageUrl, Boolean isPrivate) {

    }
}
