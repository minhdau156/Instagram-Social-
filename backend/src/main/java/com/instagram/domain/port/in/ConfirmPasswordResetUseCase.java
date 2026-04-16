package com.instagram.domain.port.in;

public interface ConfirmPasswordResetUseCase {
    void confirmPasswordReset(Command command);

    record Command(String token, String newPassword) {
    }
}
