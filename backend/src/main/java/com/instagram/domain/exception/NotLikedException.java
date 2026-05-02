package com.instagram.domain.exception;

import java.util.UUID;

public class NotLikedException extends RuntimeException {
    public NotLikedException(String targetType, UUID targetId) {
        super(String.format("User has not liked %s '%s'", targetType, targetId));
    }
}
