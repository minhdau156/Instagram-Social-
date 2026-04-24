package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.UUID;

/**
 * Input port — use case for retrieving a single post by its ID.
 */
public interface GetPostUseCase {

    Post getPost(Query query);

    record Query(UUID id, UUID currentUserId) {
    }
}
