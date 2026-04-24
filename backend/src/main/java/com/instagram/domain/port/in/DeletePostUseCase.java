package com.instagram.domain.port.in;

import java.util.UUID;

/**
 * Input port — use case for soft-deleting a post.
 */
public interface DeletePostUseCase {

    void deletePost(Command command);

    record Command(UUID id, UUID requesterId) {
    }
}
