CREATE TABLE IF NOT EXISTS users (
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR                                 NOT NULL,
    requester_id bigint references users (id) on delete cascade,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT primary_key_id_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items (
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY        NOT NULL,
    name        VARCHAR                                        NOT NULL,
    description VARCHAR                                        NOT NULL,
    available   BOOLEAN                                        NOT NULL,
    owner_id    BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    request_id  BIGINT REFERENCES requests (id) ON DELETE CASCADE,
    CONSTRAINT primary_key_id_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY        NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE                    NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE                    NOT NULL,
    item_id    BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    booker_id  BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    status     VARCHAR,
    CONSTRAINT primary_key_id_booking PRIMARY KEY (id)
);

create table if not exists comments (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY        NOT NULL,
    text         VARCHAR                                        NOT NULL,
    item_id      BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    author_id    BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);