package com.instagram.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MediaItemRequest(
        @NotBlank(message = "mediaKey must not be blank") String mediaKey,

        @NotBlank(message = "mediaType must not be blank") @Pattern(regexp = "IMAGE|VIDEO", message = "mediaType must be IMAGE or VIDEO") String mediaType,

        Integer width,
        Integer height,
        Integer duration,
        Long fileSizeBytes,

        @NotBlank(message = "sortOrder must not be blank") String sortOrder) {

}
