package com.instagram.domain.exception;

import java.util.UUID;

/**
 * Thrown when a requested post cannot be found (or has been soft-deleted).
 * Pure domain exception — maps to HTTP 404 in the web adapter layer.
 */
public class PostNotFoundException extends RuntimeException {

    private final UUID postId;

    public PostNotFoundException(UUID postId) {
        super("Post not found with id: " + postId);
        this.postId = postId;
    }

    public UUID getPostId() {
        return postId;
    }
}
