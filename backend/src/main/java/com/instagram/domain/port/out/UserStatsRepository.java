package com.instagram.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.instagram.domain.model.UserStats;

public interface UserStatsRepository {
    Optional<UserStats> findByUserId(UUID userId);

    void incrementFollowerCount(UUID userId);

    void decrementFollowerCount(UUID userId);

    void incrementFollowingCount(UUID userId);

    void decrementFollowingCount(UUID userId);
}
