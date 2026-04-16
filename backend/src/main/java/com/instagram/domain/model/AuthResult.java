package com.instagram.domain.model;

public record AuthResult(String accessToken, String refreshToken, long expiresIn) {
}
