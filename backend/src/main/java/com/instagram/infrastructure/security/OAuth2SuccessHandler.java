package com.instagram.infrastructure.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.instagram.domain.model.User;
import com.instagram.domain.model.UserStatus;
import com.instagram.domain.port.out.TokenPort;
import com.instagram.domain.port.out.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final UserRepository userRepository;
    private final TokenPort tokenPort;

    public OAuth2SuccessHandler(UserRepository userRepository, TokenPort tokenPort) {
        this.userRepository = userRepository;
        this.tokenPort = tokenPort;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        // Find existing user or create a new one; save() returns the persisted entity with its ID
        User resolvedUser = userRepository.findByEmail(email).orElseGet(() -> {
            log.info("New OAuth2 user — provisioning account for email={}", email);
            User newUser = User.builder()
                    .email(email)
                    .username(oauth2User.getAttribute("name"))
                    .passwordHash(null)          // OAuth2 users have no password (nullable per domain model)
                    .status(UserStatus.ACTIVE)
                    .build();
            return userRepository.save(newUser); // returns persisted User with generated ID
        });

        String accessToken  = tokenPort.generateAccessToken(resolvedUser.getId(), "ROLE_USER");
        String refreshToken = tokenPort.generateRefreshToken(resolvedUser.getId());

        String redirectUrl = frontendUrl
                + "/oauth2/callback?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        log.debug("OAuth2 login success for userId={}, redirecting to frontend", resolvedUser.getId());
        response.sendRedirect(redirectUrl);
    }
}
