# TASK-4.3 — Out-Port: LikeRepository

## Overview

Define the `LikeRepository` out-port interface — the boundary contract between the domain layer and the persistence layer for all like-related operations. `LikeService` depends only on this interface, never on JPA directly.

## Requirements

- Lives in `domain/port/out/` — no Spring or JPA annotations.
- All method signatures use domain types (`UUID`, `boolean`) — not JPA entities.
- This interface is implemented by `LikePersistenceAdapter` in the adapter layer (TASK-4.7).

## File Location

```
backend/src/main/java/com/instagram/domain/port/out/LikeRepository.java
```

---

## Checklist

- [ ] Create `LikeRepository.java` interface with the following methods:

  ```java
  public interface LikeRepository {

      // ─── Post Likes ──────────────────────────────────────────────────────────

      /**
       * Persists a new post like. Throws AlreadyLikedException if already liked.
       */
      void likePost(UUID postId, UUID userId);

      /**
       * Removes a post like. Throws NotLikedException if the like does not exist.
       */
      void unlikePost(UUID postId, UUID userId);

      /**
       * Returns true if the given user has already liked the given post.
       */
      boolean hasLikedPost(UUID postId, UUID userId);

      /**
       * Returns a paginated list of users who have liked the post,
       * ordered by most recent first.
       */
      List<UUID> findPostLikerIds(UUID postId, Pageable pageable);

      // ─── Comment Likes ────────────────────────────────────────────────────────

      /**
       * Persists a new comment like.
       */
      void likeComment(UUID commentId, UUID userId);

      /**
       * Removes a comment like.
       */
      void unlikeComment(UUID commentId, UUID userId);

      /**
       * Returns true if the given user has already liked the given comment.
       */
      boolean hasLikedComment(UUID commentId, UUID userId);
  }
  ```

- [ ] Import `java.util.List`, `java.util.UUID`, `org.springframework.data.domain.Pageable`
- [ ] Add Javadoc on each method explaining preconditions and postconditions
- [ ] Verify method names align with `LikeService` implementation in TASK-4.5
