package com.instagram.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PostJpaEntity}.
 *
 * <p>Internal to the persistence adapter — never referenced directly from
 * the application or domain layers.</p>
 */
public interface PostJpaRepository extends JpaRepository<PostJpaEntity, UUID> {

    /** Find a single post that has not been soft-deleted. */
    Optional<PostJpaEntity> findByIdAndDeletedAtIsNull(UUID id);

    /** All non-deleted posts, newest first. */
    List<PostJpaEntity> findByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    /** Non-deleted posts belonging to a specific user, newest first. */
    List<PostJpaEntity> findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}
