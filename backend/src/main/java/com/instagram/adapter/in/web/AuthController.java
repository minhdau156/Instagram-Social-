package com.instagram.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.domain.port.in.RegisterUserUseCase;
import com.instagram.domain.port.in.LoginUseCase;
import com.instagram.domain.port.in.LogoutUseCase;
import com.instagram.domain.port.in.RefreshTokenUseCase;
import com.instagram.domain.port.in.RequestPasswordResetUseCase;

import jakarta.validation.Valid;

import com.instagram.adapter.in.web.dto.ApiResponse;
import com.instagram.domain.model.User;
import com.instagram.domain.port.in.ConfirmPasswordResetUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ConfirmPasswordResetUseCase confirmPasswordResetUseCase;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest req) {
        User user = registerUserUseCase.register(new RegisterUserUseCase.Command(
                req.username(), req.email(), req.password(), req.fullName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(UserResponse.from(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse authResponse = loginUseCase.login(new LoginUseCase.Command(req.email(), req.password()));
        return ResponseEntity.ok(ApiResponse.ok(authResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshRequest req) {
        AuthResponse authResponse = refreshTokenUseCase
                .refreshToken(new RefreshTokenUseCase.Command(req.refreshToken()));
        return ResponseEntity.ok(ApiResponse.ok(authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshRequest req) {
        logoutUseCase.logout(new LogoutUseCase.Command(req.refreshToken()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@Valid @RequestBody PasswordResetRequest req) {
        requestPasswordResetUseCase.requestPasswordReset(new RequestPasswordResetUseCase.Command(req.email()));
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest req) {
        confirmPasswordResetUseCase
                .confirmPasswordReset(new ConfirmPasswordResetUseCase.Command(req.token(), req.newPassword()));
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}
