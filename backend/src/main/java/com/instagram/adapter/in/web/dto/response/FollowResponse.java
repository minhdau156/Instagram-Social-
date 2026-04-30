package com.instagram.adapter.in.web.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.instagram.domain.model.Follow;

public record FollowResponse(
        UUID followerId,
        UUID followingId,
        String status,
        Instant createdAt) {

    public static FollowResponse from(Follow follow) {
        return new FollowResponse(
                follow.getFollowerId(),
                follow.getFollowingId(),
                follow.getStatus().name(),
                follow.getCreatedAt());
    }
}
