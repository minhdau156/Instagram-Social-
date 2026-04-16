package com.instagram.domain.model;

public record UserProfile(User user, UserStats stats, boolean isFollowing) {
}
