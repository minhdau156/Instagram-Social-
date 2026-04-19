package com.instagram.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.adapter.in.web.dto.ApiResponse;
import com.instagram.domain.model.User;
import com.instagram.domain.port.in.GetUserProfileUseCase;
import com.instagram.domain.port.in.UpdateProfileUseCase;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import com.instagram.adapter.in.web.dto.UserResponse;
import com.instagram.adapter.in.web.dto.UserProfileResponse;
import com.instagram.adapter.in.web.dto.UpdateProfileRequest;
import com.instagram.domain.model.UserProfile;
import com.instagram.domain.model.PrivacyLevel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;

    private UUID currentUserId() {
        return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile() {
        UserProfile profile = getUserProfileUseCase.getUserProfile(new GetUserProfileUseCase.Query(null, currentUserId()));
        return ResponseEntity.ok(ApiResponse.ok(UserResponse.from(profile.user())));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(@Valid @RequestBody UpdateProfileRequest req) {
        PrivacyLevel privacyLevel = req.isPrivate() != null ? (req.isPrivate() ? PrivacyLevel.PRIVATE : PrivacyLevel.PUBLIC) : null;
        User user = updateProfileUseCase.updateProfile(new UpdateProfileUseCase.Command(
                currentUserId().toString(), req.fullName(), req.bio(), null, null, privacyLevel));
        return ResponseEntity.ok(ApiResponse.ok(UserResponse.from(user)));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(@PathVariable String username) {
        UserProfile profile = getUserProfileUseCase.getUserProfile(new GetUserProfileUseCase.Query(username, currentUserId()));
        return ResponseEntity.ok(ApiResponse.ok(UserProfileResponse.from(profile)));
    }

}
