package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.List;
import java.util.UUID;

/**
 * Input port — use case for listing posts with pagination support.
 */
public interface ListPostsUseCase {

    /**
     * List all non-deleted posts ordered by {@code created_at DESC}.
     *
     * @param page zero-based page index
     * @param size number of items per page
     */
    List<Post> listAllPosts(int page, int size);

    /**
     * List non-deleted posts belonging to a specific user.
     *
     * @param userId target user's UUID
     * @param page   zero-based page index
     * @param size   number of items per page
     */
    List<Post> listPostsByUser(UUID userId, int page, int size);
}
