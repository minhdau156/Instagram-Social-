package com.instagram.domain.port.out;

public interface EmailPort {

    /**
     * Send a password-reset link to the given email address.
     * Full SMTP implementation is wired in TASK-1.12.
     */
    void sendPasswordResetEmail(String email, String resetToken);
}
