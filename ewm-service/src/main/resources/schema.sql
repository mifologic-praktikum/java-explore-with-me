CREATE TABLE IF NOT EXISTS USERS
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(55)                             NOT NULL,
    email VARCHAR(50) UNIQUE                      NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CATEGORIES
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(55) UNIQUE                      NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS LOCATIONS
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    description        VARCHAR(7000),
    category_id        BIGINT                                  NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    state              VARCHAR(10),
    confirmed_requests INTEGER,
    created_on         TIMESTAMP,
    initiator_id       BIGINT                                  NOT NULL,
    location_id        BIGINT                                  NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INTEGER,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN,
    views              BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_user_id FOREIGN KEY (initiator_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_category_id FOREIGN KEY (category_id) REFERENCES CATEGORIES (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_location_id FOREIGN KEY (location_id) REFERENCES LOCATIONS (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP,
    event_id     BIGINT,
    requester_id BIGINT,
    status       VARCHAR(10),

    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_event_id FOREIGN KEY (event_id) REFERENCES EVENTS (id) ON DELETE CASCADE,
    CONSTRAINT fk_requests_user_id FOREIGN KEY (requester_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMPILATIONS
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title    VARCHAR(120)                            NOT NULL,
    pinned   BOOLEAN,
    event_id BIGINT,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT fk_compilations_event_id FOREIGN KEY (event_id) REFERENCES EVENTS (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMPILATION_EVENTS
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,

    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES COMPILATIONS (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_id FOREIGN KEY (event_id) REFERENCES EVENTS (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment_text VARCHAR(1000)                            NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    user_id      BIGINT                                  NOT NULL,
    created      TIMESTAMP                               NOT NULL,
    updated      TIMESTAMP,
    state        VARCHAR(10)                             NOT NULL,

    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_events_id FOREIGN KEY (event_id) REFERENCES EVENTS (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_users_id FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

