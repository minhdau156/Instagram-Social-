package com.instagram.domain.model;

import java.time.Instant;
import java.util.UUID;

public class CommentLike {
    private UUID commentId;
    private UUID userId;
    private Instant createdAt;

    private CommentLike() {

    }

    public UUID getCommentId() {
        return commentId;
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
        private final CommentLike commentLike = new CommentLike();

        public Builder commentId(UUID commentId) {
            commentLike.commentId = commentId;
            return this;
        }

        public Builder userId(UUID userId) {
            commentLike.userId = userId;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            commentLike.createdAt = createdAt;
            return this;
        }

        public CommentLike build() {
            if (commentLike.commentId == null) {
                throw new IllegalStateException("commentId is required");
            }
            if (commentLike.userId == null) {
                throw new IllegalStateException("userId is required");
            }
            if (commentLike.createdAt == null) {
                commentLike.createdAt = Instant.now();
            }
            return commentLike;
        }
    }

    public static CommentLike of(UUID commentId, UUID userId) {
        return builder()
                .commentId(commentId)
                .userId(userId)
                .createdAt(Instant.now())
                .build();
    }

}
