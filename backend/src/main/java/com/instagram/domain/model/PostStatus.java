package com.instagram.domain.model;

/**
 * Represents the lifecycle state of a post.
 * Pure domain enum — no framework dependencies.
 */
public enum PostStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED,
    DELETED
}
