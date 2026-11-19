CREATE TABLE sales_requests (
    id                         BIGSERIAL PRIMARY KEY,
    first_name                 VARCHAR(100)  NOT NULL,
    last_name                  VARCHAR(100)  NOT NULL,
    phone                      VARCHAR(50),
    email                      VARCHAR(255)  NOT NULL,
    company_name               VARCHAR(255)  NOT NULL,
    country                    VARCHAR(100),
    projected_monthly_volume   VARCHAR(100),
    industry                   VARCHAR(255),
    company_website            VARCHAR(500),
    is_ecommerce_platform      VARCHAR(50),
    business_description       TEXT,
    created_at                 TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);
