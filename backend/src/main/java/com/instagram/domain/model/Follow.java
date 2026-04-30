package com.instagram.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Follow {
    private UUID id;
    private UUID followerId;
    private UUID followingId;
    private FollowStatus status;
    private Instant createdAt;

    private Follow() {

    }

    public UUID getId() {
        return id;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getFollowingId() {
        return followingId;
    }

    public FollowStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Follow follow = new Follow();

        public Builder id(UUID id) {
            follow.id = id;
            return this;
        }

        public Builder followerId(UUID followerId) {
            follow.followerId = followerId;
            return this;
        }

        public Builder followingId(UUID followingId) {
            follow.followingId = followingId;
            return this;
        }

        public Builder status(FollowStatus status) {
            follow.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            follow.createdAt = createdAt;
            return this;
        }

        public Follow build() {
            if (follow.followerId == null)
                throw new IllegalStateException("followerId is required");
            if (follow.followingId == null)
                throw new IllegalStateException("followingId is required");
            if (follow.status == null)
                throw new IllegalStateException("status is required");
            return follow;
        }
    }

    private Follow copy() {
        return builder()
                .id(id)
                .followerId(followerId)
                .followingId(followingId)
                .status(status)
                .createdAt(createdAt)
                .build();
    }

    public Follow withAccepted() {
        Follow f = this.copy();
        f.status = FollowStatus.ACCEPTED;
        return f;
    }

    public static Follow of(UUID followerId, UUID followingId, FollowStatus status) {
        return new Builder()
                .id(UUID.randomUUID())
                .followerId(followerId)
                .followingId(followingId)
                .status(status)
                .createdAt(Instant.now())
                .build();
    }

}
