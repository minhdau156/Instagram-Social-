package com.instagram.domain.port.out;

public interface PasswordHashPort {

    /**
     * Hash a raw password. Full BCrypt implementation wired in TASK-1.12.
     */
    String hash(String rawPassword);

    /**
     * Return true if rawPassword matches the stored hash.
     */
    boolean verify(String rawPassword, String storedHash);
}
