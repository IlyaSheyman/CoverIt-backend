package coverit.ImageClient.constants;

import org.apache.http.util.NetUtils;

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
        EMPTY_SOUNDS,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT
    }
    String NONE_VIBE_PROMPT = "Come up with a prompt for visual AI to generate an image. The prompt must contain a artistic description, which you must construct from the words below. The final image should reflect this description at the level of visual details and mood. Your answer should be 3-4 sentences long.";
    String DANCING_FLOOR_PROMPT = null;
    String NATURE_PROMPT = null;
    String EMPTY_SOUNDS_PROMPT = null;
    String CAMPFIRE_CALMNESS_PROMPT = null;
    String TOUGH_AND_STRAIGHT_PROMPT = null;
}