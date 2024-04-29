INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
VALUES ('john.doe@example.com', 'subscriber1', 'Password1!', true, true, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('jane.smith@example.com', 'jane.smith', 'Password2@', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('michael.johnson@example.com', 'michael.johnson', 'Password3#', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('emily.brown@example.com', 'emily.brown', 'Password4$', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('daniel.davis@example.com', 'subscriber2', 'Password5%', true, true, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('olivia.wilson@example.com', 'olivia.wilson', 'Password6^', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('james.taylor@example.com', 'subscriber4', 'Password7&', true, true, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('emma.martinez@example.com', 'emma.martinez', 'Password8*', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('william.anderson@example.com', 'william.anderson', 'Password9(', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP),
       ('sophia.garcia@example.com', 'sophia.garcia', 'Password10)', true, false, 0, 0, 0, 0, CURRENT_TIMESTAMP);