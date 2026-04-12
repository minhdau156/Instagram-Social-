package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.UUID;

/**
 * Input port — use case for updating an existing post's editable fields.
 */
public interface UpdatePostUseCase {

    Post updatePost(UpdatePostCommand command);

    /**
     * @param id       post to update
     * @param caption  new caption (null = no change)
     * @param location new location (null = no change)
     */
    record UpdatePostCommand(UUID id, String caption, String location) {}
}
