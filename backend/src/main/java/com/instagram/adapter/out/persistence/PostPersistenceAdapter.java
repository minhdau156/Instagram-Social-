package com.instagram.adapter.out.persistence;

import com.instagram.domain.model.Post;
import com.instagram.domain.model.PostStatus;
import com.instagram.domain.port.out.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence adapter — implements the {@link PostRepository} output port
 * using Spring Data JPA.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Map domain {@link Post} ↔ {@link PostJpaEntity}</li>
 *   <li>Delegate persistence operations to {@link PostJpaRepository}</li>
 * </ul>
 * </p>
 */
@Component
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository jpaRepository;

    public PostPersistenceAdapter(PostJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ── PostRepository (output port) ─────────────────────────────────────── //

    @Override
    public Post save(Post post) {
        PostJpaEntity entity = toEntity(post);
        PostJpaEntity saved  = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Post> findById(UUID id) {
        return jpaRepository.findByIdAndDeletedAtIsNull(id)
                .map(this::toDomain);
    }

    @Override
    public List<Post> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(pageable)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Post> findByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId, pageable)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // ── Mapping ──────────────────────────────────────────────────────────── //

    private PostJpaEntity toEntity(Post post) {
        return PostJpaEntity.builder()
                .id(post.getId())                       // null → Hibernate generates UUID
                .userId(post.getUserId())
                .caption(post.getCaption())
                .location(post.getLocation())
                .status(post.getStatus() != null ? post.getStatus() : PostStatus.PUBLISHED)
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .saveCount(post.getSaveCount())
                .shareCount(post.getShareCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    private Post toDomain(PostJpaEntity e) {
        return Post.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .caption(e.getCaption())
                .location(e.getLocation())
                .status(e.getStatus())
                .viewCount(e.getViewCount())
                .likeCount(e.getLikeCount())
                .commentCount(e.getCommentCount())
                .saveCount(e.getSaveCount())
                .shareCount(e.getShareCount())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .build();
    }
}
