package coverit.image_client.constants;

public interface Constants {
    String SPOTIFY_REGEX = "^(https?://)?(www\\.)?(open\\.spotify\\.com/playlist/)[a-zA-Z0-9]+.*$";
    String YANDEX_MUSIC_REGEX = "^(https?://)?(music\\.yandex\\.ru/album/)[0-9]+.*$";

    int MIN_LINK_LENGTH = 5;
    int MAX_LINK_LENGTH = 115;


    int MIN_PLAYLIST_TITLE_LENGTH = 1;
    int MAX_PLAYLIST_TITLE_LENGTH = 50;
    int MAX_PLAYLIST_SIZE = 30;

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        EMPTY_SOUNDS,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT
    }
    String GPT_REQUEST_MAIN = "Create a short fictional image description based on the following words without using them. ";
    String ABSTRACT_GPT_CONSTRAINT = "It must describe an abstract, visual phenomenon. ";
    String GPT_REQUEST_SETTINGS = "Your answer should consist of 1-2 sentences. Words list: ";
    String GPT_NONE_VIBE = "You must describe [what is shown - briefly], [where it is - with a detailed description], [what the mood is - in 1-2 adjectives]. Do not use interrogative sentences, provide only a description";
    String NONE_VIBE_DALLE_INSTRUCTION = "The picture should be lo-fi and ascetic, clear, avoid neon colors and text.";

    String GPT_DANCING_FLOOR = "You must describe [what is shown - in a few words], [where it is - with description]. The action should take place in the future, include futuristic objects. The final description should be ascetic, light, minimalistic, without neon details. Do not use interrogative sentences, provide only a description. Do not use words that could violate copyright. ";
    String DANCING_FLOOR_INSTRUCTION = "The picture should be abstract, light, nature, mostly in white colors";
    String NATURE_PROMPT = null;
    String EMPTY_SOUNDS_PROMPT = null;
    String CAMPFIRE_CALMNESS_PROMPT = null;
    String TOUGH_AND_STRAIGHT_PROMPT = null;
}