package com.instagram.domain.port.in.follow;

import java.util.List;
import java.util.UUID;

import com.instagram.domain.model.Follow;

public interface GetFollowRequestsUseCase {
    List<Follow> getFollowRequests(Query query);

    record Query(UUID userId) {
    }
}
