package com.instagram.domain.exception;

import java.util.UUID;

public class FollowRequestNotFoundException extends RuntimeException {
    public FollowRequestNotFoundException(UUID followRequestId) {
        super("Follow request not found: " + followRequestId);
    }
}
