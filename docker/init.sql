CREATE SCHEMA IF NOT EXISTS document;

SET search_path TO document;

CREATE TABLE data
(
    id          SERIAL PRIMARY KEY,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(255) DEFAULT 'username'        NOT NULL,
    modified_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_by VARCHAR(255) DEFAULT 'username'        NOT NULL,
    version     INT          DEFAULT 0                 NOT NULL,
    is_deleted  BOOLEAN      DEFAULT FALSE             NOT NULL,
    timestamp   BIGINT                                 NOT NULL,
    value       INT                                    NOT NULL,
    hash        VARCHAR(2)                             NOT NULL
);