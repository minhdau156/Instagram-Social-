package com.instagram.adapter.in.web.dto;

import com.instagram.domain.model.PrivacyLevel;
import com.instagram.domain.model.User;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    String fullName,
    String bio,
    String avatarUrl,
    boolean isPrivate,
    boolean isVerified
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(), 
            user.getUsername(), 
            user.getEmail(),
            user.getFullName(), 
            user.getBio(), 
            user.getProfilePictureUrl(),
            user.getPrivacyLevel() == PrivacyLevel.PRIVATE, 
            user.isVerified()
        );
    }
}
