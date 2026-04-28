package com.instagram.domain.port.in.follow;

import com.instagram.domain.model.Follow;

import java.util.UUID;

public interface ApproveFollowRequestUseCase {
    Follow approve(Command command);

    record Command(UUID followingId, UUID followRequestId) {
    }
}
