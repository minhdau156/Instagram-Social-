package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.UUID;

/**
 * Input port — use case for retrieving a single post by its ID.
 */
public interface GetPostUseCase {

    /**
     * @param id the post UUID
     * @return the post domain object
     * @throws com.instagram.domain.exception.PostNotFoundException if not found or deleted
     */
    Post getPost(UUID id);
}
