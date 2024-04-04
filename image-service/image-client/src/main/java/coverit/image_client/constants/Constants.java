package coverit.image_client.constants;

public interface Constants {
    String SPOTIFY_REGEX = "^(https?://)?(www\\.)?(open\\.spotify\\.com/playlist/)[a-zA-Z0-9]+.*$";
    String YANDEX_MUSIC_REGEX = "^(https?://)?(music\\.yandex\\.ru/album/)[0-9]+.*$";

    int MIN_PLAYLIST_TITLE_LENGTH = 1;
    int MAX_PLAYLIST_TITLE_LENGTH = 50;
    int MAX_PLAYLIST_SIZE = 30;

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        BREAKING_DOWN,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        ENDLESS_JOY
    }

    //Constants for all playlists
    String GPT_REQUEST_MAIN = "Create a short fictional image description based on the following words without using them. ";
    String ABSTRACT_GPT_CONSTRAINT = "It must describe an abstract, visual phenomenon. ";
    String GPT_REQUEST_SETTINGS = "Your answer should consist of 1-2 sentences. Words list: ";

    //None vibe settings
    String GPT_NONE_VIBE = "You must describe [what is shown: object or action in 2-3 words], [surrounding in 4-5 words], [what the mood of the image is in 2-3 adjectives]. Do not use interrogative sentences, provide only a description";
    String NONE_VIBE_DALLE_INSTRUCTION = "The picture should be ascetic, clear, avoid text.";

    //Dancing floor vibe settings
    String GPT_DANCING_FLOOR = "You must describe [what is shown: object or action in 2-3 words], [surrounding in 4-5 words]. The action should take place in the future, include futuristic objects. The final description should be ascetic, light, minimalistic, without neon details. Do not use interrogative sentences, provide only a description. Do not use words that could violate copyright. ";
    String DANCING_FLOOR_INSTRUCTION = "The picture should be abstract, light, nature, mostly in white colors";

    //TODO Other settings
    String NATURE_PROMPT = null;
    String EMPTY_SOUNDS_PROMPT = null;
    String CAMPFIRE_CALMNESS_PROMPT = null;
    String TOUGH_AND_STRAIGHT_PROMPT = null;
}