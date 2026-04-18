package com.instagram.adapter.out.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SmtpEmailAdapterTest {

    private SmtpEmailAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SmtpEmailAdapter();
        ReflectionTestUtils.setField(adapter, "frontendUrl", "http://localhost:5173");
    }

    @Test
    void shouldSendPasswordResetEmail() {
        // Given
        String toEmail = "test@example.com";
        String resetToken = "token123";

        // When / Then
        // We only assert that it does not throw any exceptions since it's a log stub
        assertDoesNotThrow(() -> adapter.sendPasswordResetEmail(toEmail, resetToken));
    }
}
