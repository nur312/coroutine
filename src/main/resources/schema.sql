DROP TABLE IF EXISTS user_entity;

CREATE TABLE user
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255),
    username VARCHAR(255),
    email    VARCHAR(255),
    quote    VARCHAR(255)
);