package com.instagram.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for submitting a new password with a reset token")
public record PasswordResetConfirmRequest(
        @Schema(description = "Token received via email", example = "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6") @NotBlank String token,

        @Schema(description = "New password (8-72 characters)", example = "NewSecurePass123!") @NotBlank @Size(min = 8, max = 72) String newPassword) {
}
