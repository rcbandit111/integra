CREATE TABLE signups (
    id                    BIGSERIAL PRIMARY KEY,
    email                 VARCHAR(255)  NOT NULL,
    password_hash         VARCHAR(255)  NOT NULL,
    first_name            VARCHAR(100)  NOT NULL,
    last_name             VARCHAR(100)  NOT NULL,
    company               VARCHAR(255)  NOT NULL,
    registration_number   VARCHAR(100),
    industry              VARCHAR(255),
    country               VARCHAR(100),
    created_at            TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT signups_email_unique UNIQUE (email)
);
