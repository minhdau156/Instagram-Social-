package com.instagram.adapter.in.web.dto.response;

import java.util.UUID;

import com.instagram.domain.model.UserSummary;

public record UserSummaryResponse(
        UUID id,
        String username,
        String fullName,
        String profilePictureUrl,
        boolean isVerified,
        boolean isFollowing) {

    public static UserSummaryResponse from(UserSummary summary) {
        return new UserSummaryResponse(
                summary.id(),
                summary.username(),
                summary.fullName(),
                summary.profilePictureUrl(),
                summary.isVerified(),
                summary.isFollowing());
    }
}
