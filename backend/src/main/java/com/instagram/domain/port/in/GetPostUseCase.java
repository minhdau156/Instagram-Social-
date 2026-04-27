package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;
import com.instagram.domain.model.PostMedia;

import java.util.List;
import java.util.UUID;

/**
 * Input port — use case for retrieving a single post by its ID.
 */
public interface GetPostUseCase {

    Post getPost(Query query);

    List<PostMedia> getPostMedia(UUID postId);

    record Query(UUID id, UUID currentUserId) {
    }
}
