package coverit.image_client.constants;

public interface Constants {
    String SPOTIFY_REGEX = "^(https?://)?(www\\.)?(open\\.spotify\\.com/playlist/)[a-zA-Z0-9]+.*$";
    String YANDEX_MUSIC_REGEX = "^(https?://)?(music\\.yandex\\.ru/album/)[0-9]+.*$";

    int MIN_PLAYLIST_TITLE_LENGTH = 1;
    int MAX_PLAYLIST_TITLE_LENGTH = 50;
    int MAX_PLAYLIST_SIZE = 20;

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        BREAKING_DOWN,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        ENDLESS_JOY
    }

    //Constants for all playlists
    String GPT_REQUEST_MAIN = "Create a short fictional image description based on all words from words list without using them. ";
    String ABSTRACT_GPT_CONSTRAINT = "It must describe an abstract, visual phenomenon. ";

    String GPT_SETTINGS_AVOID = "Do not use interrogative sentences, provide only a description. Do not use words that could violate copyright. ";
    String GPT_SETTINGS_LENGTH = "Your answer should consist of 1-2 sentences. Words list: ";

    //None vibe settings
    String GPT_NONE_VIBE = "You must describe [what is shown: object or action in 2-3 words], [surrounding in 3-5 words], [what the mood of the image is in 2-4 adjectives]. ";
    String NONE_VIBE_DALLE_INSTRUCTION = "The picture should be abstract, ascetic, avoid text.";

    //Dancing floor vibe settings
    String GPT_DANCING_FLOOR = "You must describe one futuristic object [what is shown: object or action in 2-3 words], [surrounding in 3-5 words]. The action should take place in the future. The final description should be ascetic, ethereal, dreamy. ";
    String DANCING_FLOOR_INSTRUCTION = "The picture should be gentle, light, mostly in white colors";

    //Nature doesn't care vibe settings
    String GPT_NATURE = "You must describe natural landscape: [what is shown in 2-3 words], [description of natural surrounding in 3-5 words]. The final description should be dreamy, organic, ethereal. ";
    String NATURE_INSTRUCTION = "The picture should include natural landscape, be organic, fantasy";

    //Breaking down vibe settings
    String GPT_BREAKING_DOWN = "You must describe sad and nostalgic landscape: [what is shown in 2-3 words], [description of gloomy surrounding in 3-5 words]. The final description should be dreary, melancholic. Do not use interrogative sentences, provide only a description. ";
    String BREAKING_DOWN_INSTRUCTION = "The picture should be 3D render, foggy, blurred, minimalistic. ";

    //Campfire calmness vibe settings
    String GPT_CAMPFIRE_CALMNESS = "You must describe calm and peaceful situation: [what is shown in 2-3 words], [description of gentle surrounding in 3-5 words]. The final description should be tranquil, quite. ";
    String CAMPFIRE_CALMNESS_INSTRUCTION = "The picture should be in pastel colors, calm, serene, motion blur. ";

    //Tough and straight vibe settings
    String GPT_TOUGH_AND_STRAIGHT = "You must describe [what is shown in 2-3 words], [description of gray and brutal surrounding in 3-5 words]. The final description should be tough and dark. ";
    String TOUGH_AND_STRAIGHT_INSTRUCTION = "The picture should be fuzzy, haunting, gloomy, brutal and minimalistic. ";

    //Endless joy vibe settings
    String GPT_ENDLESS_JOY = "You must describe [what is shown in 2-3 words], [description of bright and joyful surrounding in 3-5 words]. The final description should be vibrant, expressive and elegant. ";;
    String ENDLESS_JOY_INSTRUCTION = "The surrounding should be dreamy autochrome, saturated, exciting, and minimalistic. ";
}