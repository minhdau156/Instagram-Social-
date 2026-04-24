package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.UUID;

/**
 * Input port — use case for updating an existing post's editable fields.
 */
public interface UpdatePostUseCase {

    Post updatePost(Command command);

    record Command(UUID id, UUID requesterId, String caption, String location) {
    }
}
