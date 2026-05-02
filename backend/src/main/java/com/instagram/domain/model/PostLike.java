package com.instagram.domain.model;

import java.time.Instant;
import java.util.UUID;

public class PostLike {
    private UUID postId;
    private UUID userId;
    private Instant createdAt;

    private PostLike() {

    }

    public UUID getPostId() {
        return postId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final PostLike postLike = new PostLike();

        public Builder postId(UUID postId) {
            postLike.postId = postId;
            return this;
        }

        public Builder userId(UUID userId) {
            postLike.userId = userId;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            postLike.createdAt = createdAt;
            return this;
        }

        public PostLike build() {
            if (postLike.postId == null) {
                throw new IllegalStateException("postId is required");
            }
            if (postLike.userId == null) {
                throw new IllegalStateException("userId is required");
            }
            if (postLike.createdAt == null) {
                postLike.createdAt = Instant.now();
            }
            return postLike;
        }
    }

    public static PostLike of(UUID postId, UUID userId) {
        return builder()
                .postId(postId)
                .userId(userId)
                .createdAt(Instant.now())
                .build();
    }

}
