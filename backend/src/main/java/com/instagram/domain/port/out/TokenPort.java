package com.instagram.domain.port.out;

import java.util.UUID;

public interface TokenPort {
    String generateAccessToken(UUID userId, String role);

    String generateRefreshToken(UUID userId);

    Optional<UUID> validateAccessToken(String token);

    Optional<UUID> validateRefreshToken(String token);

}
