package com.instagram.domain.port.in.follow;

import java.util.List;
import java.util.UUID;

import com.instagram.domain.model.UserSummary;

public interface GetFollowRequestsUseCase {
    List<UserSummary> getFollowRequests(Query query);

    record Query(UUID userId) {
    }
}
