package com.instagram.domain.model;

/**
 * Maps to the PostgreSQL {@code account_status} enum in the {@code users} table.
 * Values must stay in sync with V1__initial_schema.sql.
 */
public enum UserStatus {
    ACTIVE,
    SUSPENDED,
    DEACTIVATED,
    PENDING_VERIFICATION
}
