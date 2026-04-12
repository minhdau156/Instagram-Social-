package com.instagram.adapter.in.web.dto;

import com.instagram.domain.model.Post;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Read-only response DTO for post data.
 * Constructed from a domain {@link Post} via the static factory {@link #from(Post)}.
 */
public record PostResponse(
        UUID id,
        UUID userId,
        String caption,
        String location,
        String status,
        long viewCount,
        int likeCount,
        int commentCount,
        int saveCount,
        int shareCount,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    /** Map a domain Post to the wire-format response. */
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUserId(),
                post.getCaption(),
                post.getLocation(),
                post.getStatus() != null ? post.getStatus().name() : null,
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getSaveCount(),
                post.getShareCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
