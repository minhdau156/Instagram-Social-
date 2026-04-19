package com.instagram.adapter.in.web.dto;

import com.instagram.domain.model.AuthResult;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    long expiresIn
) {
    public static AuthResponse from(AuthResult result) {
        return new AuthResponse(result.accessToken(), result.refreshToken(), result.expiresIn());
    }
}
