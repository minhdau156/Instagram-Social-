package com.instagram.domain.exception;

public class PasswordResetTokenExpiredException extends RuntimeException {
    public PasswordResetTokenExpiredException() {
        super("Password reset token has expired or is invalid");
    }
}
