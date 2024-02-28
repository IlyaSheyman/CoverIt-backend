package coverit.ImageClient.constants;

public interface Constants {
    String SPOTIFY_REGEX = "^(https?://)?(www\\.)?(open\\.spotify\\.com/playlist/)[a-zA-Z0-9]+.*$";
    String YANDEX_MUSIC_REGEX = "^(https?://)?(music\\.yandex\\.ru/album/)[0-9]+.*$";

    int MIN_LINK_LENGTH = 5;
    int MAX_LINK_LENGTH = 115;

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        SOUND_OF_NOTHING,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        GARDEN_OF_NOSTALGIA
    }
}
