package main_service.card.cover.server.service;

import lombok.RequiredArgsConstructor;
import main_service.card.cover.client.CoverClient;
import main_service.constants.Constants;
import org.springframework.stereotype.Service;

import static main_service.constants.Constants.Vibe.DANCING_FLOOR;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;

    @Override
    public void getCover(int userId, String url, Constants.Vibe vibe, Boolean isPrivate) {

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
}
