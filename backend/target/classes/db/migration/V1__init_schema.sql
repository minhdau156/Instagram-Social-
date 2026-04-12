-- =============================================================================
-- V1 — Initial Schema: users + posts tables
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ---------------------------------------------------------------------------
-- users (minimal — FK target for posts.user_id)
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id                  UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    username            VARCHAR(50)  NOT NULL UNIQUE,
    email               VARCHAR(255) UNIQUE,
    phone_number        VARCHAR(20)  UNIQUE,
    password_hash       VARCHAR(255),
    full_name           VARCHAR(150) NOT NULL DEFAULT '',
    bio                 VARCHAR(150),
    profile_picture_url TEXT,
    website_url         TEXT,
    account_status      VARCHAR(30)  NOT NULL DEFAULT 'active',
    privacy_level       VARCHAR(20)  NOT NULL DEFAULT 'public',
    is_verified         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    last_login_at       TIMESTAMPTZ,

    CONSTRAINT chk_contact CHECK (email IS NOT NULL OR phone_number IS NOT NULL)
);

-- ---------------------------------------------------------------------------
-- posts
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS posts (
    id            UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id       UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    caption       TEXT,
    location      VARCHAR(255),
    status        VARCHAR(50)  NOT NULL DEFAULT 'PUBLISHED',
    view_count    BIGINT       NOT NULL DEFAULT 0,
    like_count    INT          NOT NULL DEFAULT 0,
    comment_count INT          NOT NULL DEFAULT 0,
    save_count    INT          NOT NULL DEFAULT 0,
    share_count   INT          NOT NULL DEFAULT 0,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    deleted_at    TIMESTAMPTZ,

    CONSTRAINT chk_post_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED', 'DELETED'))
);

CREATE INDEX IF NOT EXISTS idx_posts_user    ON posts (user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_posts_created ON posts (created_at DESC) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_posts_status  ON posts (status) WHERE deleted_at IS NULL;
