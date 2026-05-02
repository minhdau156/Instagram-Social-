package com.instagram.domain.exception;

import java.util.UUID;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(String targetType, UUID targetId) {
        super(String.format("User has already liked %s '%s'", targetType, targetId));
    }
}
