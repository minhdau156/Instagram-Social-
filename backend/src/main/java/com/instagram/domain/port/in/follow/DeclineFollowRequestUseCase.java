package com.instagram.domain.port.in.follow;

import java.util.UUID;

public interface DeclineFollowRequestUseCase {
    void decline(Command command);

    record Command(UUID followingId, UUID followRequestId) {
    }
}
