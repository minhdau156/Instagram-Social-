package com.instagram.domain.port.out;

import com.instagram.domain.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port — repository abstraction for post persistence.
 *
 * <p>Implemented by {@code PostPersistenceAdapter} in the adapter/out layer.
 * The domain and application layers depend ONLY on this interface — never on JPA.</p>
 */
public interface PostRepository {

    /**
     * Persists a new post or updates an existing one.
     *
     * @param post the domain post (id may be null for new posts)
     * @return the saved post, including the generated id if newly created
     */
    Post save(Post post);

    /**
     * Find a non-deleted post by its UUID.
     */
    Optional<Post> findById(UUID id);

    /**
     * List all non-deleted posts, ordered by creation date descending.
     */
    List<Post> findAll(int page, int size);

    /**
     * List non-deleted posts for a specific user, ordered by creation date descending.
     */
    List<Post> findByUserId(UUID userId, int page, int size);
}
