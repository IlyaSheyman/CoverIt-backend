package coverit.ImageClient.constants;

public interface Constants {
    String SPOTIFY_REGEX = "^(https?://)?(www\\.)?(open\\.spotify\\.com/playlist/)[a-zA-Z0-9]+.*$";
    String YANDEX_MUSIC_REGEX = "^(https?://)?(music\\.yandex\\.ru/album/)[0-9]+.*$";

    int MIN_LINK_LENGTH = 5;
    int MAX_LINK_LENGTH = 115;


    int MIN_PLAYLIST_TITLE_LENGTH = 1;
    int MAX_PLAYLIST_TITLE_LENGTH = 50;
    int MAX_PLAYLIST_SIZE = 100;

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
    String NONE_VIBE_PROMPT = "Come up with a prompt for visual AI to generate an image. The prompt must contain a artistic description, which you must construct from the words below. The final image should reflect this description at the level of visual details and mood. Your answer should be 3-4 sentences long.";
}
