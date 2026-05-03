package com.instagram.adapter.out.persistence.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment_likes")
public class CommentLikeJpaEntity {

    @EmbeddedId
    private CommentLikeId id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    // Required by JPA
    public CommentLikeJpaEntity() {
    }

    public CommentLikeJpaEntity(CommentLikeId id) {
        this.id = id;
    }

    public CommentLikeId getId() {
        return id;
    }

    public void setId(CommentLikeId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CommentLikeJpaEntity))
            return false;
        CommentLikeJpaEntity that = (CommentLikeJpaEntity) o;
        return java.util.Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
