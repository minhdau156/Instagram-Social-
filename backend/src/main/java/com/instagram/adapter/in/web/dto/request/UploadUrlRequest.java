package com.instagram.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UploadUrlRequest(
        @NotBlank(message = "filename must not be blank") String filename, // original filename, used to derive
        // extension

        @NotBlank(message = "contentType must not be blank") @Pattern(regexp = "image/(jpeg|png|webp|gif)|video/(mp4|quicktime)", message = "Unsupported content type") String contentType // MIME
// type,
// e.g.
// image/jpeg

) {

}
