package com.instagram.adapter.in.web.dto.response;

import com.instagram.domain.model.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Extended user profile response including social statistics")
public record UserProfileResponse(
        @Schema(description = "Basic user details") UserResponse user,

        @Schema(description = "Number of posts created by the user", example = "42") long postCount,

        @Schema(description = "Number of followers", example = "1000") long followerCount,

        @Schema(description = "Number of accounts the user is following", example = "500") long followingCount,

        @Schema(description = "Whether the current authenticated user is following this user", example = "true") boolean isFollowing) {
    public static UserProfileResponse from(UserProfile profile) {
        return new UserProfileResponse(
                UserResponse.from(profile.user()),
                profile.stats().postCount(),
                profile.stats().followerCount(),
                profile.stats().followingCount(),
                profile.isFollowing());
    }
}
