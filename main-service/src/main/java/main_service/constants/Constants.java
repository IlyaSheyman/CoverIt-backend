package main_service.constants;

import jakarta.validation.constraints.Size;
import main_service.exception.model.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface Constants {

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //USER PROPERTIES
    int MIN_USERNAME_LENGTH = 2;
    int MAX_USERNAME_LENGTH = 30;

    int MIN_USER_PASSWORD_LENGTH = 8;
    int MAX_USER_PASSWORD_LENGTH = 27;

    int MIN_USER_EMAIL_LENGTH = 6;
    int MAX_USER_EMAIL_LENGTH = 254;

    //PLAYLIST PROPERTIES
    int MIN_PLAYLIST_TITLE_LENGTH = 1;
    int MAX_PLAYLIST_TITLE_LENGTH = 50;

    int MIN_TRACK_TITLE_LENGTH = 1;
    int MAX_TRACK_TITLE_LENGTH = 200;
    int MIN_AUTHOR_NAME_LENGTH = 1;
    int MAX_AUTHOR_NAME_LENGTH = 200;

    int MIN_LINK_LENGTH = 5;
    int MAX_LINK_LENGTH = 1000;

    //RELEASE PROPERTIES
    int MIN_RELEASE_TITLE_LENGTH = 1;
    int MAX_RELEASE_TITLE_LENGTH = 50;

    int MIN_MOOD_SIZE = 1;
    int MAX_MOOD_SIZE = 3;

    int MIN_OBJECT_SIZE = 2;
    int MAX_OBJECT_SIZE = 255;

    int MIN_SURROUNDING_SIZE = 2;
    int MAX_SURROUNDING_SIZE = 255;

    int MIN_COVER_DESCRIPTION_SIZE = 1;
    int MAX_COVER_DESCRIPTION_SIZE  = 3;

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        BREAKING_DOWN,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        ENDLESS_JOY
    }

    enum SortBy {
        CREATED,
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        BREAKING_DOWN,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        ENDLESS_JOY
    }
}