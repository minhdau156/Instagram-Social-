package com.instagram.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for user registration")
public record RegisterRequest(
        @Schema(description = "Unique username (3-30 characters)", example = "john_doe") @NotBlank @Size(min = 3, max = 30) String username,

        @Schema(description = "Valid email address", example = "john@example.com") @NotBlank @Email String email,

        @Schema(description = "Password (8-72 characters)", example = "SecurePass123!") @NotBlank @Size(min = 8, max = 72) String password,

        @Schema(description = "Full display name", example = "John Doe") @NotBlank @Size(max = 100) String fullName) {
}
