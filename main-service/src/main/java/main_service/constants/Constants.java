package main_service.constants;

public interface Constants {

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    int SHELF_LIFE = 0;

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

    int MIN_LINK_LENGTH = 5;
    int MAX_LINK_LENGTH = 1000;

    //RELEASE PROPERTIES
    int MIN_RELEASE_TITLE_LENGTH = 1;
    int MAX_RELEASE_TITLE_LENGTH = 50;

    int MIN_MOOD_SIZE = 1;
    int MAX_MOOD_SIZE = 5;

    int MIN_OBJECT_SIZE = 2;
    int MAX_OBJECT_SIZE = 255;

    int MIN_SURROUNDING_SIZE = 2;
    int MAX_SURROUNDING_SIZE = 255;

    int MIN_COVER_DESCRIPTION_SIZE = 1;
    int MAX_COVER_DESCRIPTION_SIZE = 5;

    int SUBSCRIPTION_GENERATIONS_LIMIT = 100;
    int HIFI_LIMIT_RELEASE = 2;
    int LOFI_LIMIT_RELEASE = 4;

    int HIFI_LIMIT_PLAYLIST = 1;
    int LOFI_LIMIT_PLAYLIST = 3;

    int DELETE_COVER_DELAY = 43200000;

    enum Vibe {
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        BREAKING_DOWN,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        ENDLESS_JOY
    }

    enum Filters {
        ABSTRACT,
        NOT_ABSTRACT,
        LO_FI,
        HI_FI,
        DANCING_FLOOR,
        NATURE_DOES_NOT_CARE,
        BREAKING_DOWN,
        CAMPFIRE_CALMNESS,
        TOUGH_AND_STRAIGHT,
        ENDLESS_JOY
    }

    enum Mood {
        ENERGETIC,
        MELANCHOLIC,
        UPBEAT,
        SERENE,
        JOYFUL,
        MYSTERIOUS,
        UPLIFTING,
        REFLECTIVE,
        DRAMATIC,
        DREAMY,
        INTENSE,
        PEACEFUL,
        WHIMSICAL,
        LIVELY,
        ROMANTIC,
        SOOTHING,
        NOSTALGIC,
        EXCITING,
        HAUNTING,
        HOPEFUL,
        ETHEREAL,
        ENCHANTING,
        SENTIMENTAL,
        TRANQUIL,
        FUNKY,
        BROODING,
        BLISSFUL,
        DYNAMIC,
        CINEMATIC,
        MYSTICAL,
        ATMOSPHERIC,
        CHEERY,
        EUPHORIC,
        TRIUMPHANT,
        TENDER,
        CAPTIVATING,
        ENIGMATIC,
        PENSIVE,
        GROOVY,
        MAJESTIC,
        QUIRKY,
        RADIANT,
        RHYTHMIC,
        SUSPENSEFUL,
        WISTFUL,
        SPOOKY,
        EXHILARATING,
        HYPNOTIC,
        PLAYFUL,
        REGAL,
        SILKY,
        SULTRY,
        BREEZY,
        BRIGHT,
        BUOYANT,
        CAREFREE,
        CELEBRATORY,
        CHEERFUL,
        CHILL,
        CLEVER,
        CONFIDENT,
        COOL,
        COZY,
        DANCING,
        DELICATE,
        DELIGHTFUL,
        DETERMINED,
        DREAMLIKE,
        DRIVING,
        ECSTATIC,
        EMOTIONAL,
        EMPOWERING,
        EPIC,
        FIERCE,
        FREE,
        FRESH,
        FRIENDLY,
        FUN,
        GENTLE,
        GLORIOUS,
        GRACEFUL,
        HAPPY,
        HARMONIOUS,
        HEARTFELT,
        HEAVENLY,
        HONEST,
        HUMOROUS,
        INSPIRING,
        JAZZY,
        JUBILANT,
        LIGHT_HEARTED,
        LUSH,
        MAGICAL,
        MOTIVATIONAL,
        OPTIMISTIC,
        PASSIONATE,
        REASSURING,
        SASSY,
        SENSUAL,
        SILLY,
        SMOOTH,
        SOFT,
        SOPHISTICATED,
        SPIRITED,
        SPRIGHTLY,
        STIRRING,
        SWEET,
        TRANQUILIZING,
        URGENT,
        VIBRANT,
        WARM,
        WHOLESOME,
        WILD
    }

    enum Style {
        STREET_ART,
        PENCIL_SKETCH,
        PENCIL_DRAWING,
        WOODCUT,
        CHARCOAL_SKETCH,
        ETCHING,
        CRAYON,
        CHILDREN_DRAWING,
        WATERCOLOR,
        COLOURED_PENCIL,
        DETAILED,
        AIRBRUSH,
        PASTELS,
        OIL_PAINTING,
        VECTOR_ART,
        COLLAGE,
        PHOTOCOLLAGE,
        MAGAZINE_COLLAGE,
        SCREEN_PRINTING,
        LOW_POLY,
        LAYERED_PAPER,
        STICKER_ILLUSTRATION,
        STORYBOOK,
        DIGITAL_PAINTING,
        BLUEPRINT,
        PATENT_DRAWING,
        CUTAWAY,
        IKEA_MANUAL,
        BOTANICAL_ILLUSTRATION,
        MYTHOLOGICAL_MAP,
        VOYNICH_MANUSCRIPT,
        SCIENTIFIC_DIAGRAM,
        INSTRUCTION_MANUAL,
        VORONI_DIAGRAM,
        RENDER_3D,
        HOUDINI_3D,
        OCTANE_3D,
        FELT_PIECES,
        FABRIC_PATTERN,
        CINEMA_4D,
        BLENDER,
        BLACK_VELVET,
        SCRATCH_ART,
        FOIL_ART,
        GOLD_ON_BLACK,
        PERLER_BEADS,
        SCREENSHOT_FROM_GAME,
        SCREENSHOT_FROM_CONSOLE,
        TATTOO,
        COMIC_BOOK_ART,
        VINTAGE_DISNEY,
        PIXEL_ART,
        ANIME,
        CAVE_PAINTINGS,
        PRE_HISTORIC,
        PRIMITIVE,
        MURAL,
        TOMB,
        FRESCO,
        REGISTER,
        HIEROGLYPHICS,
        PAPYRUS,
        MOSAIC,
        ON_WOOD,
        ICON,
        HALO,
        ARTEFACT,
        ANCIENT,
        MANISCRIPT,
        VELLUM,
        REALISM,
        ART_NOUVEAU,
        IMPRESSIONISM,
        POST_IMPRESSIONISM,
        NEOCLASSICISM,
        SYMBOLIST_PAINTING,
        SYMBOLISM,
        DREAMLIKE,
        ART_DECO,
        VINTAGE,
        STREAMLINE,
        MODERNE,
        LUXURY,
        POSTER,
        ABSTRACT_EXPRESSIONISM,
        GEOMETRIC,
        CONSTRUCTIVIST,
        CONSTRUCTIVISM,
        DESIGN,
        DADAISM, DADAIST,
        ABSURD,
        NONSENSE,
        ASSEMBLAGE,
        CUT_UP,
        PHOTOMONTAGE,
        CUBISM,
        NEOPLASTICISM,
        EXPRESSIONISM,
        FAUVISM,
        FUTURISM,
        DYNAMISM,
        METAPHYSICAL_PAINTING,
        SURREALISM,
        POP_ART,
        GRAFFITI,
        URBAN_PUBLIC_ART,
        SUPREMATISM,
        MURALISM,
        NEO_EXPRESSIONISM,
        ORPHISM,
        STREET_PHOTOGRAPHY,
        CANDID,
        FLANEUR,
        UNPOSED,
        BRONZE_STATUE,
        MARBLE_STATUE,
        TERRACOTTA_WARRIORS,
        JADE_SCULPTURE,
        BUTTER_SCULPTURE,
        SAND_SCULPTURE,
        PLANT_SCULPTURE,
        ICE_CARVING,
        PLASTIC,
        FIBERGLASS,
        FASHION_PHOTOGRAPHY,
        THEATRICAL,
        COSPLAY,
        NEEDLE_FELTING,
        DIORAMA,
        APPLIQUÉ_NEEDLEWORK,
        POPUP_BOOK,
        PAPER_EMBOSSING,
        PAPIER_MACHE,
        KNITTING_PATTERNS,
        CERAMICS,
        CLAY,
        POTTERY,
        SCULPTURE,
        STAINED_GLASS,
        GLASS_PAPERWEIGHT,
        CRYSTAL_FIGURE,
        CYANOTYPE,
        BLACK_AND_WHITE,
        REDSCALE_PHOTOGRAPHY,
        MONOCHROME_PHOTOGRAPHY,
        RETRO_FILTERED,
        COLOUR_SPLASH,
        ONE_COLOUR,
        INFRARED_PHOTOGRAPHY,
        SOLARISED,
        BLEACH_BYPASS,
        MUTED,
        PHOTOGRAPHY_3D,
        Kodachrome,
        Autochrome,
        Lomography,
        CCTV,
        FOOTAGE,
        DASHCAM,
        DISPOSABLE_CAMERA,
        OVERSATURATED,
        HUE_SHIFTED,
        DAGUERROTYPE,
        POLAROID,
        INSTAX,
        SOFT_FOCUS,
        CAMERA_OBSCURA,
        PINHOLE,
        FUZZY,
        DOUBLE_EXPOSURE,
        WARM_LIGHTING,
        COLD_LIGHTING,
        FLUORESCENT_LIGHTING,
        FLASH_PHOTOGRAPHY,
        NEUTRAL_LIGHTING,
        FLAT_LIGHTING,
        EVEN_LIGHTING,
        CORPORATE_LIGHTING,
        PROFESSIONAL_LIGHTING,
        AMBIENT_LIGHTING,
        DRAMATIC_LIGHTING,
        SINGLE_LIGHT_SOURCE,
        HIGH_CONTRAST,
        STUDIO_LIGHTING,
        STUDIO_PORTRAIT,
        Golden_hour,
        DUSK,
        SUNSET,
        SUNRISE,
        DIRECTIONAL_SUNLIGHT,
        MIDDAY,
        LONG_EXPOSURE,
        TELEPHOTO_LENS,
        MACRO_PHOTO,
        WIDE_ANGLE_LENS,
        FISH_EYE_LENS,
        MOTION_BLUR,
        SHARP
    }
}