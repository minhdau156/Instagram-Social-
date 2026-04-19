package com.instagram.adapter.in.web.dto;

import com.instagram.domain.model.UserProfile;

public record UserProfileResponse(
    UserResponse user,
    int postCount,
    int followerCount,
    int followingCount,
    boolean isFollowing
) {
    public static UserProfileResponse from(UserProfile profile) {
        return new UserProfileResponse(
            UserResponse.from(profile.user()),
            profile.stats().postCount(),
            profile.stats().followerCount(),
            profile.stats().followingCount(),
            profile.isFollowing()
        );
    }
}
