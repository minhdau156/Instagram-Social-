package com.instagram.domain.exception;

public class AlreadyFollowingException extends RuntimeException {
    public AlreadyFollowingException(String username) {
        super("Already following or requested to follow user: " + username);
    }
}
