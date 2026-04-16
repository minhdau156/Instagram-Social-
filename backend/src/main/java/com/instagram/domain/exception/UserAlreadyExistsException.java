package com.instagram.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String field, String value) {
        super("A user already exists with " + field + ": " + value);
    }
}