package com.instagram.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for refreshing an access token")
public record RefreshRequest(
        @Schema(description = "The valid refresh token string", example = "eyJhbGciOiJIUzI1NiIsInR5c...") @NotBlank String refreshToken) {
}
