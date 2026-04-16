package com.instagram.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException withUsername(String username) {
        return new UserNotFoundException("User not found: " + username);
    }

    public static UserNotFoundException withId(UUID id) {
        return new UserNotFoundException("User not found: " + id);
    }
}
