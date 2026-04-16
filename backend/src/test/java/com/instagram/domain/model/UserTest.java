package com.instagram.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    // ── helpers ─────────────────────────────────────────────────────────────

    private User buildActiveUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("john_doe")
                .email("john@example.com")
                .fullName("John Doe")
                .status(UserStatus.ACTIVE)
                .build();
    }

    // ── tests ────────────────────────────────────────────────────────────────

    @Test
    void builder_createsUser_withRequiredFields() {
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .username("jane_doe")
                .email("jane@example.com")
                .fullName("Jane Doe")
                .status(UserStatus.ACTIVE)
                .build();

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getUsername()).isEqualTo("jane_doe");
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getFullName()).isEqualTo("Jane Doe");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void withDeactivated_setsStatus_toInactive() {
        User original = buildActiveUser();

        User deactivated = original.withDeactivated();

        assertThat(deactivated.getStatus()).isEqualTo(UserStatus.INACTIVE);
        // original must NOT be mutated
        assertThat(original.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(deactivated).isNotSameAs(original);
    }

    @Test
    void withUpdatedProfile_returnsNewInstance_doesNotMutateOriginal() {
        User original = buildActiveUser();

        User updated = original.withUpdateProfile("John Doe", PrivacyLevel.PUBLIC, "https://example.com",
                "https://example.com/avatar.jpg");

        assertThat(updated.getFullName()).isEqualTo("John Doe");
        assertThat(updated.getPrivacyLevel()).isEqualTo(PrivacyLevel.PUBLIC);
        assertThat(updated.getWebsiteUrl()).isEqualTo("https://example.com");
        assertThat(updated.getProfilePictureUrl()).isEqualTo("https://example.com/avatar.jpg");
        assertThat(updated.getUpdatedAt()).isNotNull();
        assertThat(updated).isNotSameAs(original);

        assertThat(original.getFullName()).isEqualTo("John Doe");

    }
}
