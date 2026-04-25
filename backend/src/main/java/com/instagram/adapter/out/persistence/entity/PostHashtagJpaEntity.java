package com.instagram.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_hashtags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostHashtagJpaEntity {

    @EmbeddedId
    private PostHashtagId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private PostJpaEntity post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("hashtagId")
    @JoinColumn(name = "hashtag_id", nullable = false)
    private HashtagJpaEntity hashtag;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Embeddable
    public record PostHashtagId(UUID postId, UUID hashtagId) implements Serializable {
    }
}
