CREATE TABLE IF NOT EXISTS users
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    is_active    BOOLEAN      NOT NULL,
    city_uuid    UUID         NOT NULL,
    date_created TIMESTAMPTZ  NOT NULL,
    date_updated TIMESTAMPTZ  NOT NULL
);
