package com.instagram.domain.port.in;

public interface LogoutUseCase {
    void logout(Command command);

    record Command(String refreshToken) {
    }
}
