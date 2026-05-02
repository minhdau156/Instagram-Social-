package com.instagram.domain.port.in.like;

import java.util.UUID;

public interface UnlikePostUseCase {
    void unlike(Command command);

    record Command(UUID postId, UUID userId) {
    }
}
