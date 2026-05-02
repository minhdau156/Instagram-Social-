package com.instagram.domain.port.out;

import java.util.UUID;

public interface CommentRepository {
    void incrementLikeCount(UUID commentId);

    void decrementLikeCount(UUID commentId);

}
