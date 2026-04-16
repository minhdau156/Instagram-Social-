package com.instagram.domain.port.in;

import com.instagram.domain.model.AuthResult;

public interface RefreshTokenUseCase {
    AuthResult refreshToken(Command command);

    record Command(String refreshToken) {
    }
}
