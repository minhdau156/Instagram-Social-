package com.instagram.domain.port.in.like;

import java.util.List;
import java.util.UUID;

import com.instagram.domain.model.UserSummary;

public interface GetPostLikersUseCase {
    List<UserSummary> getLikers(Query query);

    record Query(UUID postId, UUID requestingUserId, int page, int size) {
    }
}
