CREATE TABLE partnership_requests (
    id            BIGSERIAL PRIMARY KEY,
    partner_type  VARCHAR(100)  NOT NULL,
    website_url   VARCHAR(500),
    first_name    VARCHAR(100)  NOT NULL,
    last_name     VARCHAR(100)  NOT NULL,
    phone         VARCHAR(50),
    email         VARCHAR(255)  NOT NULL,
    company_name  VARCHAR(255)  NOT NULL,
    country       VARCHAR(100),
    message       TEXT,
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);
