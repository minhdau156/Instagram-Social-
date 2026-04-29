package com.instagram.domain.port.out;

import java.util.UUID;

public interface UserStatsRepository {
    void incrementFollowerCount(UUID userId);
    void decrementFollowerCount(UUID userId);
    void incrementFollowingCount(UUID userId);
    void decrementFollowingCount(UUID userId);
}
