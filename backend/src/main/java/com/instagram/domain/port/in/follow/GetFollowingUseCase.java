package com.instagram.domain.port.in.follow;

import java.util.List;
import java.util.UUID;

import com.instagram.domain.model.UserSummary;

public interface GetFollowingUseCase {
    List<UserSummary> getFollowing(Query query);

    record Query(String targetUsername, UUID currentUserId, int page, int size) {
    }
}
