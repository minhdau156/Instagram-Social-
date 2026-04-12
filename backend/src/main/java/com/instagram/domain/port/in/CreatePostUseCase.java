package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.UUID;

/**
 * Input port — use case for creating a new post.
 *
 * <p>Implemented by {@code PostService} in the application layer.</p>
 */
public interface CreatePostUseCase {

    Post createPost(CreatePostCommand command);

    /**
     * Command object carrying the data needed to create a post.
     *
     * @param userId   owner of the post (resolved from auth context)
     * @param caption  optional descriptive text
     * @param location optional geo-tag label
     */
    record CreatePostCommand(UUID userId, String caption, String location) {}
}
