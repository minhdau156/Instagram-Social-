package com.instagram.domain.port.in;

import com.instagram.domain.model.Post;

import java.util.UUID;

import org.springframework.data.domain.Page;

public interface GetUserPostsUseCase {

    Page<Post> getUserPosts(Query query);

    record Query(UUID targetUserId, UUID currentUserId, String cursor, int limit) {
    }
}
