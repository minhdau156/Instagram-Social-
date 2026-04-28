package com.instagram.domain.port.in.follow;

import java.util.UUID;

import com.instagram.domain.model.Follow;

public interface FollowUserUseCase {
    Follow follow(Command command);

    record Command(UUID followerId, String targetUsername) {
    }
}
