package com.instagram.domain.exception;

import java.util.UUID;

public class UnauthorizedPostAccessException extends RuntimeException {

    public UnauthorizedPostAccessException(UUID postId, UUID userId) {
        super("Unauthorized access to post " + postId + " by user " + userId);
    }
}
