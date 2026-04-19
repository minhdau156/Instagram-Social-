package com.instagram.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for user login")
public record LoginRequest(
    @Schema(description = "Email or username", example = "john_doe")
    @NotBlank String identifier,
    
    @Schema(description = "User password", example = "secret123")
    @NotBlank String password
) {}
