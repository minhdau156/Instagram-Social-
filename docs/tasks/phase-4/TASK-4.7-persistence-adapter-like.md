# TASK-4.7 — Persistence Adapter: LikePersistenceAdapter

## Overview

Implement `LikePersistenceAdapter` — the adapter that bridges the domain's `LikeRepository` interface with the JPA repositories (`PostLikeJpaRepository`, `CommentLikeJpaRepository`). It translates between domain types (`UUID`) and JPA entities.

## Requirements

- Lives in `adapter/out/persistence/`.
- Annotated with `@Component` (or `@Repository`).
- Implements `LikeRepository` from TASK-4.3.
- Uses `PostLikeJpaRepository` and `CommentLikeJpaRepository` from TASK-4.6.
- Like/unlike operations that change `like_count` must happen in the **same transaction** — use `@Transactional` on the adapter methods.
- No business logic — pure data mapping and delegation.

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/LikePersistenceAdapter.java
```

---

## Checklist

- [x] Create `LikePersistenceAdapter.java` annotated with `@Component`
- [x] Implement `LikeRepository`
- [x] Inject via constructor: `PostLikeJpaRepository`, `CommentLikeJpaRepository`, `PostJpaRepository`, `CommentJpaRepository`
- [x] Declare all fields `private final`

- [x] Implement `likePost(UUID postId, UUID userId)`:
  ```java
  @Transactional
  public void likePost(UUID postId, UUID userId) {
      PostLikeId id = new PostLikeId(postId, userId);
      postLikeJpaRepository.save(new PostLikeJpaEntity(id, Instant.now()));
      postJpaRepository.incrementLikeCount(postId);
  }
  ```

- [x] Implement `unlikePost(UUID postId, UUID userId)`:
  ```java
  @Transactional
  public void unlikePost(UUID postId, UUID userId) {
      postLikeJpaRepository.deleteByIdPostIdAndIdUserId(postId, userId);
      postJpaRepository.decrementLikeCount(postId);
  }
  ```

- [x] Implement `hasLikedPost(UUID postId, UUID userId)`:
  ```java
  public boolean hasLikedPost(UUID postId, UUID userId) {
      return postLikeJpaRepository.existsByIdPostIdAndIdUserId(postId, userId);
  }
  ```

- [x] Implement `findPostLikerIds(UUID postId, Pageable pageable)`:
  ```java
  public List<UUID> findPostLikerIds(UUID postId, Pageable pageable) {
      return postLikeJpaRepository
          .findByIdPostIdOrderByCreatedAtDesc(postId, pageable)
          .stream()
          .map(e -> e.getId().getUserId())
          .collect(Collectors.toList());
  }
  ```

- [x] Implement `likeComment(UUID commentId, UUID userId)`:
  - Same pattern as `likePost` using `CommentLikeId`, `CommentLikeJpaRepository`, `commentJpaRepository.incrementLikeCount`

- [x] Implement `unlikeComment(UUID commentId, UUID userId)`:
  - Same pattern as `unlikePost`

- [x] Implement `hasLikedComment(UUID commentId, UUID userId)`:
  - Delegates to `commentLikeJpaRepository.existsByIdCommentIdAndIdUserId(...)`

- [x] Annotate `likePost`, `unlikePost`, `likeComment`, `unlikeComment` with `@Transactional` to ensure count update and like record are in the same transaction
