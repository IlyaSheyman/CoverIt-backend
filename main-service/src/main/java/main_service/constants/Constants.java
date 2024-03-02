package main_service.constants;

import main_service.exception.model.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface Constants {

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    int MIN_USERNAME_LENGTH = 2;
    int MAX_USERNAME_LENGTH = 250;

    int MIN_USER_PASSWORD_LENGTH = 8;
    int MAX_USER_PASSWORD_LENGTH = 27;

    int MIN_USER_EMAIL_LENGTH = 6;
    int MAX_USER_EMAIL_LENGTH = 254;

    String FROM = "0";
    String SIZE = "10";

    int MIN_PLAYLIST_TITLE_LENGTH = 1;
    int MAX_PLAYLIST_TITLE_LENGTH = 50;

    int MIN_TRACK_TITLE_LENGTH = 1;
    int MAX_TRACK_TITLE_LENGTH = 200;
    int MIN_AUTHOR_NAME_LENGTH = 1;
    int MAX_AUTHOR_NAME_LENGTH = 200;

    int MIN_LINK_LENGTH = 5;
    int MAX_LINK_LENGTH = 400;

    static Pageable checkPageable(Integer from, Integer size, Sort sort) {
        if (from == null) from = Integer.parseInt(FROM);
        if (size == null) size = Integer.parseInt(SIZE);
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Pageable incorrect");
        }
        if (sort != null) {
            return PageRequest.of(from / size, size, sort);
        }
        return PageRequest.of(from / size, size);
    }

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        SOUND_OF_NOTHING,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        GARDEN_OF_NOSTALGIA,
        FUTURE_IS_NOW,
        ROUTINE_SOUNDS
    }
}