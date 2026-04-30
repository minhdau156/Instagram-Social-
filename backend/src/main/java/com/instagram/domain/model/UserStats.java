package com.instagram.domain.model;

import java.util.UUID;

public record UserStats(UUID userId, long postCount, long followerCount, long followingCount) {

    public static UserStats zero(UUID userId) {
        return new UserStats(userId, 0, 0, 0);
    }
}
