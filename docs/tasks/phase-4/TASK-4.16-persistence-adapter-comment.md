# TASK-4.16 — Persistence Adapter: CommentPersistenceAdapter

## Overview

Implement `CommentPersistenceAdapter` — the adapter that bridges the domain's `CommentRepository` interface with `CommentJpaRepository`. It handles all domain ↔ JPA entity translation for the comment feature.

## Requirements

- Lives in `adapter/out/persistence/`.
- Annotated with `@Component`.
- Implements `CommentRepository` from TASK-4.12.
- Uses `CommentJpaRepository` from TASK-4.15.
- All write operations are `@Transactional`.
- No business logic — pure data mapping and delegation.

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/CommentPersistenceAdapter.java
```

---

## Checklist

- [ ] Create `CommentPersistenceAdapter.java` annotated with `@Component`
- [ ] Implement `CommentRepository`
- [ ] Inject via constructor: `CommentJpaRepository`, `PostJpaRepository`
- [ ] Declare all fields `private final`

- [ ] Implement `save(Comment comment)`:
  ```java
  @Transactional
  public Comment save(Comment comment) {
      CommentJpaEntity entity = CommentJpaEntity.fromDomain(comment);
      return commentJpaRepository.save(entity).toDomain();
  }
  ```

- [ ] Implement `findById(UUID commentId)`:
  ```java
  public Optional<Comment> findById(UUID commentId) {
      return commentJpaRepository.findById(commentId)
          .map(CommentJpaEntity::toDomain);
  }
  ```

- [ ] Implement `findByPostId(UUID postId, Pageable pageable)`:
  ```java
  public Page<Comment> findByPostId(UUID postId, Pageable pageable) {
      return commentJpaRepository.findTopLevelByPostId(postId, pageable)
          .map(CommentJpaEntity::toDomain);
  }
  ```

- [ ] Implement `findByParentId(UUID parentId, Pageable pageable)`:
  ```java
  public Page<Comment> findByParentId(UUID parentId, Pageable pageable) {
      return commentJpaRepository.findRepliesByParentId(parentId, pageable)
          .map(CommentJpaEntity::toDomain);
  }
  ```

- [ ] Implement `incrementReplyCount(UUID parentCommentId)`:
  - Annotate with `@Transactional`
  - Delegate to `commentJpaRepository.incrementReplyCount(parentCommentId)`

- [ ] Implement `decrementReplyCount(UUID parentCommentId)`:
  - Same pattern

- [ ] Implement `incrementLikeCount(UUID commentId)`:
  - Delegate to `commentJpaRepository.incrementLikeCount(commentId)`

- [ ] Implement `decrementLikeCount(UUID commentId)`:
  - Same pattern

- [ ] Implement `incrementPostCommentCount(UUID postId)`:
  - Delegate to `postJpaRepository.incrementCommentCount(postId)`
  - Ensure `PostJpaRepository` has this `@Modifying @Query` method (add it in TASK-4.6 or here)

- [ ] Implement `decrementPostCommentCount(UUID postId)`:
  - Delegate to `postJpaRepository.decrementCommentCount(postId)`
