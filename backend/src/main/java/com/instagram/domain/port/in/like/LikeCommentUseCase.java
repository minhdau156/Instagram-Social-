package com.instagram.domain.port.in.like;

import java.util.UUID;

public interface LikeCommentUseCase {
    void likeComment(Command command);

    record Command(UUID commentId, UUID userId) {
    }
}
