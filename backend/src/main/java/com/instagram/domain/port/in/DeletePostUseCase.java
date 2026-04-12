package com.instagram.domain.port.in;

import java.util.UUID;

/**
 * Input port — use case for soft-deleting a post.
 */
public interface DeletePostUseCase {

    /**
     * Soft-deletes the post by setting {@code deleted_at} and
     * changing status to {@code DELETED}. The record is retained in the DB.
     *
     * @param id the post UUID to delete
     * @throws com.instagram.domain.exception.PostNotFoundException if already deleted or not found
     */
    void deletePost(UUID id);
}
