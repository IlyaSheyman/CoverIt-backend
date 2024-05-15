-- Вставка данных в таблицу playlist без cover_id
INSERT INTO playlist (title, vibe, url, is_private, is_saved, user_id, hifi_generations_left,
                      lofi_generations_left, saved_at, created_at)
VALUES ('playlist 1', 'ENDLESS_JOY', 'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6',
        false, true, 1, 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 2', 'DANCING_FLOOR', 'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6',
        true, false, 2, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 3', 'NATURE_DOES_NOT_CARE',
        'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6', false, true, 3, 1, 0,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 4', 'BREAKING_DOWN', 'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6',
        true, false, 4, 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 5', 'CAMPFIRE_CALMNESS',
        'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6', false, true, 4, 1, 2,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 6', 'TOUGH_AND_STRAIGHT',
        'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6', true, false, 4, 1, 0,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 7', 'ENDLESS_JOY', 'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6',
        false, true, 2, 0, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 8', 'DANCING_FLOOR', 'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6',
        true, false, 6, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 9', 'NATURE_DOES_NOT_CARE',
        'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6', false, true, 7, 0, 2,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('playlist 10', 'BREAKING_DOWN', 'https://open.spotify.com/playlist/5eamAjsBHoXLEL9x64ttBu?si=874909480b1a46b6',
        true, false, 2, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Вставка данных в таблицу playlist_cover
INSERT INTO playlist_cover (playlist_id, cover_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10);
