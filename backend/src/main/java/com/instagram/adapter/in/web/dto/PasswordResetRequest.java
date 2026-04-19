package com.instagram.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for requesting a password reset")
public record PasswordResetRequest(
    @Schema(description = "Registered email address", example = "john@example.com")
    @NotBlank @Email String email
) {}
