INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'john.doe@example.com',
       'subscriber1',
       'Password1!',
       true,
       true,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'john.doe@example.com' OR username = 'subscriber1');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'jane.smith@example.com',
       'jane.smith',
       'Password2@',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'jane.smith@example.com' OR username = 'jane.smith');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'michael.johnson@example.com',
       'michael.johnson',
       'Password3#',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'michael.johnson@example.com' OR username = 'michael.johnson');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'emily.brown@example.com',
       'emily.brown',
       'Password4$',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'emily.brown@example.com' OR username = 'emily.brown');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'daniel.davis@example.com',
       'subscriber2',
       'Password5%',
       true,
       true,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'daniel.davis@example.com' OR username = 'subscriber2');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'olivia.wilson@example.com',
       'olivia.wilson',
       'Password6^',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'olivia.wilson@example.com' OR username = 'olivia.wilson');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'james.taylor@example.com',
       'subscriber4',
       'Password7&',
       true,
       true,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'james.taylor@example.com' OR username = 'subscriber4');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'emma.martinez@example.com',
       'emma.martinez',
       'Password8*',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'emma.martinez@example.com' OR username = 'emma.martinez');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'william.anderson@example.com',
       'william.anderson',
       'Password9(',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'william.anderson@example.com' OR username = 'william.anderson');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'sophia.garcia@example.com',
       'sophia.garcia',
       'Password10)',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'sophia.garcia@example.com' OR username = 'sophia.garcia');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at)
SELECT 'admin.email@yandex.ru',
       'admin',
       '$2a$10$JylUCNUTG9I.x/cqxc8yO.qapj0Dm1GYj1.qedM6fbBrjCWkt5nuW',
       true,
       false,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin.email@yandex.ru' OR username = 'admin');

INSERT INTO users (email, username, password, is_enabled, is_subscribed, hifi_release_generations,
                   lofi_release_generations, hifi_playlist_generations, lofi_playlist_generations, enabled_at,
                   subscribed_at)
SELECT 'admin.subscriber.email@yandex.ru',
       'admin.subscriber',
       '$2a$10$JylUCNUTG9I.x/cqxc8yO.qapj0Dm1GYj1.qedM6fbBrjCWkt5nuW',
       true,
       true,
       0,
       0,
       0,
       0,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1
                  FROM users
                  WHERE email = 'admin.subscriber.email@yandex.ru' OR username = 'admin.subscriber');
