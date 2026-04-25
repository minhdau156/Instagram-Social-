package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Input port — use case for creating a new post.
 *
 * <p>
 * Implemented by {@code PostService} in the application layer.
 * </p>
 */
public interface CreatePostUseCase {

    Post createPost(Command command);

    /**
     * Represents a single media item attached to the post command.
     */
    record MediaItem(
            String mediaKey,
            String mediaType,
            Integer width,
            Integer height,
            Integer duration,
            Long fileSizeBytes,
            String sortOrder) {
    }

    record Command(
            UUID userId,
            String caption,
            String location,
            List<MediaItem> mediaItems) {
        public Command {
            Objects.requireNonNull(userId, "userId must not be null");
            if (mediaItems == null || mediaItems.isEmpty())
                throw new IllegalArgumentException("Post must have at least one media item");
        }
    }
}
