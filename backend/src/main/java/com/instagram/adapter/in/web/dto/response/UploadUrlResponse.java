package com.instagram.adapter.in.web.dto.response;

import com.instagram.domain.port.in.GenerateUploadUrlUseCase;

public record UploadUrlResponse(
        String presignedUrl, // PUT this URL directly from the browser/mobile app
        String mediaKey // Save this and include it in CreatePostRequest.mediaItems
) {
    public static UploadUrlResponse from(GenerateUploadUrlUseCase.UploadUrl u) {
        return new UploadUrlResponse(u.presignedUrl(), u.mediaKey());
    }
}
