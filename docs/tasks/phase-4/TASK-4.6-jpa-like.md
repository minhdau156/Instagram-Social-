# TASK-4.6 — JPA Entities & Repositories: Likes

## Overview

Create the JPA persistence classes for both `post_likes` and `comment_likes` tables. These are the adapter-layer representations that map relational data to domain objects. They are only referenced by `LikePersistenceAdapter`.

## Requirements

- Lives in `adapter/out/persistence/`.
- Annotated with `@Entity @Table(name = "...")`.
- Composite primary key (`post_id`/`comment_id` + `user_id`) via `@EmbeddedId`.
- Repositories extend `JpaRepository` with Spring Data query methods.
- No business logic — pure data mapping.

## File Locations

```
backend/src/main/java/com/instagram/adapter/out/persistence/
├── PostLikeId.java                  ← @Embeddable composite PK
├── PostLikeJpaEntity.java
├── PostLikeJpaRepository.java
├── CommentLikeId.java               ← @Embeddable composite PK
├── CommentLikeJpaEntity.java
└── CommentLikeJpaRepository.java
```

---

## Checklist

### `PostLikeId.java`

- [ ] Create `PostLikeId.java` annotated with `@Embeddable`:
  ```java
  @Embeddable
  public class PostLikeId implements Serializable {
      @Column(name = "post_id", nullable = false)
      private UUID postId;

      @Column(name = "user_id", nullable = false)
      private UUID userId;

      // no-arg constructor, all-args constructor, equals(), hashCode()
  }
  ```

### `PostLikeJpaEntity.java`

- [ ] Create `PostLikeJpaEntity.java`:
  ```java
  @Entity
  @Table(name = "post_likes")
  public class PostLikeJpaEntity {

      @EmbeddedId
      private PostLikeId id;

      @Column(name = "created_at", nullable = false, updatable = false)
      @CreationTimestamp
      private Instant createdAt;

      // no-arg constructor, all-args constructor, getters
  }
  ```
- [ ] `equals()` and `hashCode()` delegate to `PostLikeId`

### `PostLikeJpaRepository.java`

- [ ] Create repository interface:
  ```java
  public interface PostLikeJpaRepository extends JpaRepository<PostLikeJpaEntity, PostLikeId> {

      boolean existsByIdPostIdAndIdUserId(UUID postId, UUID userId);

      void deleteByIdPostIdAndIdUserId(UUID postId, UUID userId);

      List<PostLikeJpaEntity> findByIdPostIdOrderByCreatedAtDesc(UUID postId, Pageable pageable);
  }
  ```

---

### `CommentLikeId.java`

- [ ] Create `CommentLikeId.java` following same pattern as `PostLikeId`:
  - Fields: `commentId`, `userId`
  - Column names: `comment_id`, `user_id`

### `CommentLikeJpaEntity.java`

- [ ] Create `CommentLikeJpaEntity.java`:
  - `@Table(name = "comment_likes")`
  - `@EmbeddedId CommentLikeId id`
  - `Instant createdAt` with `@CreationTimestamp`

### `CommentLikeJpaRepository.java`

- [ ] Create repository:
  ```java
  public interface CommentLikeJpaRepository extends JpaRepository<CommentLikeJpaEntity, CommentLikeId> {

      boolean existsByIdCommentIdAndIdUserId(UUID commentId, UUID userId);

      void deleteByIdCommentIdAndIdUserId(UUID commentId, UUID userId);
  }
  ```

---

### `PostJpaRepository` & `CommentJpaRepository` Updates

- [ ] Add `@Modifying @Query` to `PostJpaRepository`:
  ```java
  @Modifying
  @Query("UPDATE PostJpaEntity p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
  void incrementLikeCount(@Param("postId") UUID postId);

  @Modifying
  @Query("UPDATE PostJpaEntity p SET p.likeCount = GREATEST(p.likeCount - 1, 0) WHERE p.id = :postId")
  void decrementLikeCount(@Param("postId") UUID postId);
  ```

- [ ] Add same `incrementLikeCount` / `decrementLikeCount` to `CommentJpaRepository`
