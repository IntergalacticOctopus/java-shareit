DROP TABLE IF EXISTS users, items, comments, bookings;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    request_id BIGINT,
    CONSTRAINT primary_key_id_item PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR NOT NULL,
    item_id BIGINT REFERENCES items(id) ON DELETE CASCADE NOT NULL,
    author_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    constraint primary_key_id_comment primary key (id)
);