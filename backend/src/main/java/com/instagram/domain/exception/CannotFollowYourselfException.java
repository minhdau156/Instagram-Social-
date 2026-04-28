package com.instagram.domain.exception;

public class CannotFollowYourselfException extends RuntimeException {
    public CannotFollowYourselfException() {
        super("You cannot follow yourself.");
    }
}
