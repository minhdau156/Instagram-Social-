package com.instagram.domain.port.in;

import com.instagram.domain.model.UserProfile;

import java.util.UUID;

public interface GetUserProfileUseCase {
    UserProfile getUserProfile(Query query);

    record Query(String targetUsername, UUID currentUserId) {
    }
}
