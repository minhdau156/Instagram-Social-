package com.instagram.infrastructure.security;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    // A valid Base64-encoded 256-bit (32-byte) secret – safe for HS256
    private static final String SECRET =
            "dGVzdC1zZWNyZXQta2V5LXRoYXQtaXMtMzItYnl0ZXM="; // "test-secret-key-that-is-32-bytes"

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "secretKey", SECRET);
        ReflectionTestUtils.setField(tokenProvider, "accessTokenExpiryMs", 900_000L);   // 15 min
        ReflectionTestUtils.setField(tokenProvider, "refreshTokenExpiryMs", 604_800_000L); // 7 days
        tokenProvider.init(); // @PostConstruct — builds signingKey from secretKey
    }

    @Test
    void generateAccessToken_returnsValidToken() {
        UUID userId = UUID.randomUUID();

        String token = tokenProvider.generateAccessToken(userId, "ROLE_USER");

        assertThat(token).isNotBlank();
    }

    @Test
    void validateAccessToken_returnsUserId_forValidToken() {
        UUID userId = UUID.randomUUID();
        String token = tokenProvider.generateAccessToken(userId, "ROLE_USER");

        Optional<UUID> result = tokenProvider.validateAccessToken(token);

        assertThat(result).isPresent().contains(userId);
    }

    @Test
    void validateAccessToken_returnsEmpty_forTamperedToken() {
        UUID userId = UUID.randomUUID();
        String token = tokenProvider.generateAccessToken(userId, "ROLE_USER");
        String tampered = token + "X"; // corrupt the signature

        Optional<UUID> result = tokenProvider.validateAccessToken(tampered);

        assertThat(result).isEmpty();
    }

    @Test
    void validateAccessToken_returnsEmpty_forCompletelyGarbageInput() {
        assertThat(tokenProvider.validateAccessToken("not.a.jwt")).isEmpty();
    }

    @Test
    void validateAccessToken_returnsEmpty_forExpiredToken() {
        // Create a provider with a 0 ms expiry to produce an instantly-expired token
        JwtTokenProvider expiredProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(expiredProvider, "secretKey", SECRET);
        ReflectionTestUtils.setField(expiredProvider, "accessTokenExpiryMs", 0L);
        ReflectionTestUtils.setField(expiredProvider, "refreshTokenExpiryMs", 0L);
        expiredProvider.init();

        String expiredToken = expiredProvider.generateAccessToken(UUID.randomUUID(), "ROLE_USER");

        assertThat(tokenProvider.validateAccessToken(expiredToken)).isEmpty();
    }

    @Test
    void generateRefreshToken_returnsValidToken() {
        UUID userId = UUID.randomUUID();

        String token = tokenProvider.generateRefreshToken(userId);

        assertThat(token).isNotBlank();
    }

    @Test
    void validateRefreshToken_returnsUserId_forValidToken() {
        UUID userId = UUID.randomUUID();
        String token = tokenProvider.generateRefreshToken(userId);

        Optional<UUID> result = tokenProvider.validateRefreshToken(token);

        assertThat(result).isPresent().contains(userId);
    }
}
