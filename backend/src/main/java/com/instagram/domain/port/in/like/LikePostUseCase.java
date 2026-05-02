package com.instagram.domain.port.in.like;

import java.util.UUID;

public interface LikePostUseCase {
    void like(Command command);

    record Command(UUID postId, UUID userId) {
    }
}
