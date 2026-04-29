package com.instagram.adapter.out.persistence;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.instagram.adapter.out.persistence.entity.FollowId;
import com.instagram.adapter.out.persistence.entity.FollowJpaEntity;
import com.instagram.adapter.out.persistence.entity.UserJpaEntity;
import com.instagram.adapter.out.persistence.repository.FollowJpaRepository;
import com.instagram.domain.model.Follow;
import com.instagram.domain.model.FollowStatus;
import com.instagram.domain.port.out.FollowRepository;

import jakarta.persistence.EntityManager;

@Component
public class FollowPersistenceAdapter implements FollowRepository {

    private final FollowJpaRepository followJpaRepository;
    private final EntityManager entityManager;

    public FollowPersistenceAdapter(FollowJpaRepository followJpaRepository, EntityManager entityManager) {
        this.followJpaRepository = followJpaRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Follow save(Follow follow) {
        FollowJpaEntity entity = toEntity(follow);
        return toDomain(followJpaRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID followerId, UUID followingId) {
        followJpaRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Follow> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId) {
        return followJpaRepository.findByIdFollowerIdAndIdFollowingId(followerId, followingId)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Follow> findFollowersByUserId(UUID userId, Pageable pageable) {
        return followJpaRepository.findByIdFollowingIdAndIsApproved(userId, true, pageable)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Follow> findFollowingByUserId(UUID userId, Pageable pageable) {
        return followJpaRepository.findByIdFollowerIdAndIsApproved(userId, true, pageable)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Follow> findPendingRequestsByFollowingId(UUID followingId) {
        return followJpaRepository.findByIdFollowingIdAndIsApprovedOrderByCreatedAtDesc(followingId, false)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countFollowersByUserId(UUID userId) {
        return followJpaRepository.countByIdFollowingIdAndIsApproved(userId, true);
    }

    @Override
    @Transactional(readOnly = true)
    public long countFollowingByUserId(UUID userId) {
        return followJpaRepository.countByIdFollowerIdAndIsApproved(userId, true);
    }

    private FollowJpaEntity toEntity(Follow follow) {
        UserJpaEntity follower = entityManager.getReference(UserJpaEntity.class, follow.getFollowerId());
        UserJpaEntity following = entityManager.getReference(UserJpaEntity.class, follow.getFollowingId());
        return FollowJpaEntity.builder()
                .id(new FollowId(follow.getFollowerId(), follow.getFollowingId()))
                .follower(follower)
                .following(following)
                .isApproved(follow.getStatus() == FollowStatus.ACCEPTED)
                .build();
    }

    private Follow toDomain(FollowJpaEntity entity) {
        UUID syntheticId = UUID.nameUUIDFromBytes(
                (entity.getId().getFollowerId().toString()
                        + entity.getId().getFollowingId().toString()).getBytes(StandardCharsets.UTF_8));
        return Follow.builder()
                .id(syntheticId)
                .followerId(entity.getId().getFollowerId())
                .followingId(entity.getId().getFollowingId())
                .status(entity.isApproved() ? FollowStatus.ACCEPTED : FollowStatus.PENDING)
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
