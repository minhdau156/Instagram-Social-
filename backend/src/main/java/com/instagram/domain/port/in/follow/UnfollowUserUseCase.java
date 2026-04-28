package com.instagram.domain.port.in.follow;

import java.util.UUID;

public interface UnfollowUserUseCase {

    void unfollow(Command command);

    record Command(UUID followerId, String targetUsername) {
    }

}
