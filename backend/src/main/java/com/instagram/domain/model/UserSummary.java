package com.instagram.domain.model;

import java.util.UUID;

public record UserSummary(
        UUID id,
        String username,
        String fullName,
        String avatarUrl,
        boolean isVerified,
        boolean isFollowing) {
}
