package com.instagram.adapter.in.web.dto;

import com.instagram.domain.model.AuthResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload containing authentication tokens")
public record AuthResponse(
    @Schema(description = "JWT Access Token for authenticating API requests", example = "eyJhbGciOiJIUzI1NiIsInR5c...")
    String accessToken,
    
    @Schema(description = "JWT Refresh Token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiIsInR5c...")
    String refreshToken,
    
    @Schema(description = "Expiration time of the access token in milliseconds", example = "3600")
    long expiresIn
) {
    public static AuthResponse from(AuthResult result) {
        return new AuthResponse(result.accessToken(), result.refreshToken(), result.expiresIn());
    }
}
