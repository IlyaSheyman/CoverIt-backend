CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR(254) UNIQUE,
username VARCHAR(250) UNIQUE
password VARCHAR(30)
);