package com.instagram.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FollowTest {

    @Test
    void builder_createsFollow_withRequiredFields() {
        UUID id = UUID.randomUUID();
        UUID followerId = UUID.randomUUID();
        UUID followingId = UUID.randomUUID();
        Instant now = Instant.now();

        Follow follow = Follow.builder()
                .id(id)
                .followerId(followerId)
                .followingId(followingId)
                .status(FollowStatus.PENDING)
                .createdAt(now)
                .build();

        assertNotNull(follow);
        assertEquals(id, follow.getId());
        assertEquals(followerId, follow.getFollowerId());
        assertEquals(followingId, follow.getFollowingId());
        assertEquals(FollowStatus.PENDING, follow.getStatus());
        assertEquals(now, follow.getCreatedAt());
    }

    @Test
    void builder_throwsException_whenFollowerIdIsNull() {
        UUID id = UUID.randomUUID();
        UUID followingId = UUID.randomUUID();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                Follow.builder()
                        .id(id)
                        .followingId(followingId)
                        .status(FollowStatus.PENDING)
                        .build()
        );

        assertEquals("followerId is required", exception.getMessage());
    }

    @Test
    void withAccepted_returnsNewInstance_withAcceptedStatus() {
        UUID followerId = UUID.randomUUID();
        UUID followingId = UUID.randomUUID();

        Follow follow = Follow.of(followerId, followingId, FollowStatus.PENDING);
        Follow acceptedFollow = follow.withAccepted();

        assertNotSame(follow, acceptedFollow);
        assertEquals(FollowStatus.PENDING, follow.getStatus());
        assertEquals(FollowStatus.ACCEPTED, acceptedFollow.getStatus());
        assertEquals(follow.getId(), acceptedFollow.getId());
        assertEquals(follow.getFollowerId(), acceptedFollow.getFollowerId());
        assertEquals(follow.getFollowingId(), acceptedFollow.getFollowingId());
        assertEquals(follow.getCreatedAt(), acceptedFollow.getCreatedAt());
    }

    @Test
    void withAccepted_doesNotMutateOriginal() {
        UUID followerId = UUID.randomUUID();
        UUID followingId = UUID.randomUUID();

        Follow follow = Follow.of(followerId, followingId, FollowStatus.PENDING);
        follow.withAccepted();

        assertEquals(FollowStatus.PENDING, follow.getStatus());
    }
}
