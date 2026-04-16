package com.instagram.domain.port.in;

public interface RequestPasswordResetUseCase {
    void requestPasswordReset(Command command);

    record Command(String email) {
    }
}
