package com.instagram.adapter.in.web.dto.request;

import java.util.List;
import java.util.UUID;

import com.instagram.domain.port.in.CreatePostUseCase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Request body for {@code POST /api/v1/posts}.
 */
public record CreatePostRequest(
                @Size(max = 2200, message = "Caption must not exceed 2200 characters") String caption,

                @Size(max = 255, message = "Location must not exceed 255 characters") String location,

                @NotEmpty(message = "A post must have at least one media item") List<@Valid MediaItemRequest> mediaItems) {
        public CreatePostUseCase.Command toCommand(UUID userId) {
                List<CreatePostUseCase.MediaItem> items = mediaItems.stream()
                                .map(m -> new CreatePostUseCase.MediaItem(
                                                m.mediaKey(), m.mediaType(), m.width(), m.height(),
                                                m.duration(), m.fileSizeBytes(), m.sortOrder()))
                                .toList();
                return new CreatePostUseCase.Command(userId, caption, location, items);
        }
}
