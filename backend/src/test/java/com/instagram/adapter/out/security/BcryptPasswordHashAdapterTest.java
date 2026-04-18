package com.instagram.adapter.out.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BcryptPasswordHashAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BcryptPasswordHashAdapter adapter;

    @Test
    void shouldHashPassword() {
        String rawPassword = "myPassword!";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = adapter.hash(rawPassword);

        assertEquals(encodedPassword, result);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void shouldVerifyPassword() {
        String rawPassword = "myPassword!";
        String storedHash = "encodedPassword123";

        when(passwordEncoder.matches(rawPassword, storedHash)).thenReturn(true);

        boolean result = adapter.verify(rawPassword, storedHash);

        assertTrue(result);
        verify(passwordEncoder).matches(rawPassword, storedHash);
    }
}
