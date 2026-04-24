package com.instagram.domain.port.in;

import java.util.UUID;

public interface GenerateUploadUrlUseCase {

    UploadUrl generateUploadUrl(Command command);

    record UploadUrl(String presignedUrl, String mediaKey) {
    }

    record Command(UUID userId, String filename, String contentType) {
    }
}
