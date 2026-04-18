package com.instagram.adapter.out.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.instagram.domain.port.out.EmailPort;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SmtpEmailAdapter implements EmailPort {

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String link = frontendUrl + "/reset-password?token=" + resetToken;
        log.info("[EMAIL STUB] Password reset link for {}: {}", toEmail, link);
    }

}
