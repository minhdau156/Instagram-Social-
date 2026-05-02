package com.instagram.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class CommentLikeTest {
    @Test
    void of_createsCommentLike_withRequiredFields() {
        UUID commentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CommentLike commentLike = CommentLike.of(commentId, userId);
        assertNotNull(commentLike);
        assertEquals(commentId, commentLike.getCommentId());
        assertEquals(userId, commentLike.getUserId());
        assertNotNull(commentLike.getCreatedAt());
    }

    @Test
    void builder_createsCommentLike_withRequiredFields() {
        UUID commentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        CommentLike commentLike = CommentLike.builder()
                .commentId(commentId)
                .userId(userId)
                .createdAt(now)
                .build();
        assertNotNull(commentLike);
        assertEquals(commentId, commentLike.getCommentId());
        assertEquals(userId, commentLike.getUserId());

    }

    @Test
    void builder_throwsException_whenCommentIdIsNull() {
        UUID userId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> CommentLike.builder()
                .userId(userId)
                .build());
    }

    @Test
    void builder_throwsException_whenUserIdIsNull() {
        UUID commentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> CommentLike.builder()
                .commentId(commentId)
                .build());
    }
}
