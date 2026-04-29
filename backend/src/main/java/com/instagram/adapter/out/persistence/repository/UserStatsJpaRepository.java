package com.instagram.adapter.out.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instagram.adapter.out.persistence.entity.UserStatsJpaEntity;

public interface UserStatsJpaRepository extends JpaRepository<UserStatsJpaEntity, UUID> {

    // Cập nhật tăng follower của target user (người được follow)
    @Modifying
    @Query("""
            UPDATE UserStatsJpaEntity us
            SET us.followerCount = us.followerCount + 1
            WHERE us.userId = :userId
            """)
    void incrementFollowerCount(@Param("userId") UUID userId);

    // Cập nhật giảm follower của target user (người bị unfollow)
    @Modifying
    @Query("""
            UPDATE UserStatsJpaEntity us
            SET us.followerCount = us.followerCount - 1
            WHERE us.userId = :userId
            """)
    void decrementFollowerCount(@Param("userId") UUID userId);

    // Cập nhật tăng following của follower (người đi follow)
    @Modifying
    @Query("""
            UPDATE UserStatsJpaEntity us
            SET us.followingCount = us.followingCount + 1
            WHERE us.userId = :userId
            """)
    void incrementFollowingCount(@Param("userId") UUID userId);

    // Cập nhật giảm following của follower (người đi unfollow)
    @Modifying
    @Query("""
            UPDATE UserStatsJpaEntity us
            SET us.followingCount = us.followingCount - 1
            WHERE us.userId = :userId
            """)
    void decrementFollowingCount(@Param("userId") UUID userId);

    // Cập nhật tăng post count khi tạo post
    @Modifying
    @Query("""
            UPDATE UserStatsJpaEntity us
            SET us.postCount = us.postCount + 1
            WHERE us.userId = :userId
            """)
    void incrementPostCount(@Param("userId") UUID userId);

    // Cập nhật giảm post count khi xóa post
    @Modifying
    @Query("""
            UPDATE UserStatsJpaEntity us
            SET us.postCount = us.postCount - 1
            WHERE us.userId = :userId
            """)
    void decrementPostCount(@Param("userId") UUID userId);
}
