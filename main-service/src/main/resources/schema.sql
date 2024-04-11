CREATE TABLE IF NOT EXISTS users
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    hifi_generations  BIGINT  NOT NULL,
    lofi_generations  BIGINT  NOT NULL,
    email             VARCHAR(254),
    username          VARCHAR(250),
    password          VARCHAR,
    verification_code VARCHAR(64),
    patron_name       VARCHAR(64),
    is_enabled        BOOLEAN NOT NULL,
    is_subscribed     BOOLEAN NOT NULL,
    enabled_at        TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS track
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    authors VARCHAR      NOT NULL,
    title   VARCHAR(200) NOT NULL
);


CREATE TABLE IF NOT EXISTS cover
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    link        VARCHAR(1000) NOT NULL,
    prompt      VARCHAR       NOT NULL,
    is_abstract BOOLEAN,
    is_lofi     BOOLEAN       NOT NULL
);


CREATE TABLE IF NOT EXISTS playlist
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    vibe        VARCHAR(100),
    url         VARCHAR(400) NOT NULL,
    is_private  BOOLEAN,
    is_saved    BOOLEAN,
    user_id     BIGINT,
    cover_id    BIGINT       NOT NULL,
    generations BIGINT       NOT NULL,
    saved_at    TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (cover_id) REFERENCES cover (id)
);

CREATE TABLE IF NOT EXISTS playlist_track
(
    playlist_id BIGINT,
    track_id    BIGINT,
    FOREIGN KEY (playlist_id) REFERENCES playlist (id),
    FOREIGN KEY (track_id) REFERENCES track (id),
    PRIMARY KEY (playlist_id, track_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id     BIGINT,
    playlist_id BIGINT,
    PRIMARY KEY (user_id, playlist_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (playlist_id) REFERENCES playlist (id)
);

CREATE TABLE IF NOT EXISTS release
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title      VARCHAR(50),
    user_id    BIGINT,
    cover_id   BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (cover_id) REFERENCES cover (id)
);
