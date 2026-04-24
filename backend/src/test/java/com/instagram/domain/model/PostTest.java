package com.instagram.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void withUpdatedCaption_returnsNewInstance_doesNotMutateOriginal() {
        Post original = Post.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .caption("Old caption")
                .likeCount(10)
                .status(PostStatus.PUBLISHED)
                .build();

        Post updated = original.withUpdateCaptionAndLocation("New caption", "New location");

        assertThat(updated.getCaption()).isEqualTo("New caption");
        assertThat(updated.getLocation()).isEqualTo("New location");
        assertThat(updated.getUpdatedAt()).isNotNull();
        assertThat(updated).isNotSameAs(original);
        assertThat(original.getCaption()).isEqualTo("Old caption"); // original unchanged
    }

    @Test
    void withSoftDelete_returnsNewInstance_doesNotMutateOriginal() {
        Post original = Post.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .status(PostStatus.PUBLISHED)
                .build();

        Post deleted = original.withSoftDelete();

        assertThat(deleted.getStatus()).isEqualTo(PostStatus.DELETED); // Soft delete → draft
        assertThat(deleted.getUpdatedAt()).isNotNull();
        assertThat(deleted).isNotSameAs(original);
        assertThat(original.getStatus()).isEqualTo(PostStatus.PUBLISHED);
    }

    @Test
    void withIncrementedLike_returnsNewInstance_doesNotMutateOriginal() {
        Post original = Post.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .likeCount(10)
                .status(PostStatus.PUBLISHED)
                .build();

        Post withLike = original.withIncrementLikeCount();

        assertThat(withLike.getLikeCount()).isEqualTo(11);
        assertThat(withLike.getUpdatedAt()).isNotNull();
        assertThat(withLike).isNotSameAs(original);
        assertThat(original.getLikeCount()).isEqualTo(10);
    }

    @Test
    void withDecrementedLike_returnsNewInstance_doesNotMutateOriginal() {
        Post original = Post.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .likeCount(10)
                .status(PostStatus.PUBLISHED)
                .build();

        Post withoutLike = original.withDecrementLikeCount();

        assertThat(withoutLike.getLikeCount()).isEqualTo(9);
        assertThat(withoutLike.getUpdatedAt()).isNotNull();
        assertThat(withoutLike).isNotSameAs(original);
        assertThat(original.getLikeCount()).isEqualTo(10);
    }
}
