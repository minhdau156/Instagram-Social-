package com.instagram.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class PostLikeTest {
    @Test
    void of_createsPostLike_withRequiredFields() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PostLike postLike = PostLike.of(postId, userId);
        assertNotNull(postLike);
        assertEquals(postId, postLike.getPostId());
        assertEquals(userId, postLike.getUserId());
        assertNotNull(postLike.getCreatedAt());
    }

    @Test
    void builder_createsPostLike_withRequiredFields() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .createdAt(now)
                .build();
        assertNotNull(postLike);
        assertEquals(postId, postLike.getPostId());
        assertEquals(userId, postLike.getUserId());

    }

    @Test
    void builder_throwsException_whenPostIdIsNull() {
        UUID userId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> PostLike.builder()
                .userId(userId)
                .build());
    }

    @Test
    void builder_throwsException_whenUserIdIsNull() {
        UUID postId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> PostLike.builder()
                .postId(postId)
                .build());
    }
}
