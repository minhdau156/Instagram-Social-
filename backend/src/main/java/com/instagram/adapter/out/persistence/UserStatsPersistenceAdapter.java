package com.instagram.adapter.out.persistence;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.instagram.adapter.out.persistence.entity.UserStatsJpaEntity;
import com.instagram.adapter.out.persistence.repository.UserStatsJpaRepository;
import com.instagram.domain.model.UserStats;
import com.instagram.domain.port.out.UserStatsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserStatsPersistenceAdapter implements UserStatsRepository {

    private final UserStatsJpaRepository jpaRepository;

    private UserStats toDomain(UserStatsJpaEntity entity) {
        return new UserStats(
                entity.getUserId(),
                entity.getFollowerCount(),
                entity.getFollowingCount(),
                entity.getPostCount());
    }

    private UserStatsJpaEntity toEntity(UserStats userStats) {
        return UserStatsJpaEntity.builder()
                .userId(userStats.userId())
                .followerCount(userStats.followerCount())
                .followingCount(userStats.followingCount())
                .postCount(userStats.postCount())
                .build();
    }

    @Override
    public void incrementFollowerCount(UUID userId) {
        jpaRepository.incrementFollowerCount(userId);
    }

    @Override
    public void decrementFollowerCount(UUID userId) {
        jpaRepository.decrementFollowerCount(userId);
    }

    @Override
    public void incrementFollowingCount(UUID userId) {
        jpaRepository.incrementFollowingCount(userId);
    }

    @Override
    public void decrementFollowingCount(UUID userId) {
        jpaRepository.decrementFollowingCount(userId);
    }

}
