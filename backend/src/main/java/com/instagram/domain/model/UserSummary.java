package com.instagram.domain.model;

import java.util.UUID;

public record UserSummary(
                UUID id,
                String username,
                String fullName,
                String profilePictureUrl,
                boolean isVerified,
                boolean isFollowing) {
}
