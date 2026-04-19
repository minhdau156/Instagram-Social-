package com.instagram.adapter.in.web.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @Size(max = 100) String fullName,
    @Size(max = 150) String bio,
    Boolean isPrivate
) {}
