package com.instagram.domain.model;

import java.util.UUID;

public record UserStats(UUID userId, int postCount, int followerCount, int followingCount) {
    public static UserStats zero(UUID userId) {
        return new UserStats(userId, 0, 0, 0);
    }
}
