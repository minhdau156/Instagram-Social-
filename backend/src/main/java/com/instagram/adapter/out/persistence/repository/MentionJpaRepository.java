package com.instagram.adapter.out.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.instagram.adapter.out.persistence.entity.MentionJpaEntity;

public interface MentionJpaRepository extends JpaRepository<MentionJpaEntity, UUID> {
    List<MentionJpaEntity> findByPostId(UUID postId);

    Page<MentionJpaEntity> findByMentionedUserId(UUID mentionedUserId, Pageable pageable);
}
