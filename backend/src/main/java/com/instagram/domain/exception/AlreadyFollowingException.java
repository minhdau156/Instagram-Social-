package com.instagram.domain.exception;

public class AlreadyFollowingException extends RuntimeException {
    public AlreadyFollowingException() {
        super("Already following or requested to follow user");
    }
}
