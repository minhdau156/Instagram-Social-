package com.instagram.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.instagram.adapter.out.persistence.entity.PostJpaEntity;
import com.instagram.domain.model.PostStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PostJpaEntity}.
 *
 * <p>
 * Internal to the persistence adapter — never referenced directly from
 * the application or domain layers.
 * </p>
 */
public interface PostJpaRepository extends JpaRepository<PostJpaEntity, UUID> {
    // Feed: user's non-deleted published posts, newest first
    Page<PostJpaEntity> findByUserIdAndStatusNot(
            UUID userId, PostStatus status, Pageable pageable);

    // Single post lookup that skips soft-deleted
    Optional<PostJpaEntity> findByIdAndStatusNot(UUID id, PostStatus status);
}
