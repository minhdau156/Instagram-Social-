package com.instagram.adapter.in.web.dto.response;

import java.time.Instant;

public record ApiResponse<T>(T data, String error, Instant timestamp) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, null, Instant.now());
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(null, message, Instant.now());
    }
}
