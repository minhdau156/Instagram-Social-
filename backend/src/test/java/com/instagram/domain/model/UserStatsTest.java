package com.instagram.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserStatsTest {

    @Test
    void zero_createsStatsWithAllCountsAsZero() {
        UUID userId = UUID.randomUUID();

        UserStats stats = UserStats.zero(userId);

        assertThat(stats.userId()).isEqualTo(userId);
        assertThat(stats.postCount()).isZero();
        assertThat(stats.followerCount()).isZero();
        assertThat(stats.followingCount()).isZero();
    }

    @Test
    void recordEquality_twoStatsWithSameValues_areEqual() {
        UUID userId = UUID.randomUUID();

        UserStats a = new UserStats(userId, 10, 200, 150);
        UserStats b = new UserStats(userId, 10, 200, 150);
        UserStats c = new UserStats(userId, 99, 200, 150);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
    }
}
