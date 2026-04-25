package com.instagram.adapter.in.web.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Request body for {@code PUT /api/v1/posts/{id}}.
 * All fields are optional — only non-null values will be applied.
 */
public record UpdatePostRequest(

                @Size(max = 2200, message = "Caption must not exceed 2200 characters") String caption,

                @Size(max = 255, message = "Location must not exceed 255 characters") String location) {
}
