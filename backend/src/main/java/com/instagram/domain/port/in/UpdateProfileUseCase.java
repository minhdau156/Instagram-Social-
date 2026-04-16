package com.instagram.domain.port.in;

import com.instagram.domain.model.PrivacyLevel;
import com.instagram.domain.model.User;

public interface UpdateProfileUseCase {
    User updateProfile(Command command);

    record Command(String userId, String fullName, String bio, String website, String profilePictureUrl,
            PrivacyLevel privacyLevel) {
    }
}
