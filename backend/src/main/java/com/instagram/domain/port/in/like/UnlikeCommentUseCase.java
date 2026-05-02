package com.instagram.domain.port.in.like;

import java.util.UUID;

public interface UnlikeCommentUseCase {
    void unlike(Command command);

    record Command(UUID commentId, UUID userId) {
    }
}
