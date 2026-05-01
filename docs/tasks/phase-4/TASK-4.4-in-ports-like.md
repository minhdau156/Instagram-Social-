# TASK-4.4 — In-Ports: Like Use Cases

## Overview

Define all use-case interfaces for the Like feature. Each use case is a single-method interface with a nested `Command` or `Query` record. These are the "driving" ports — `LikeController` calls them and `LikeService` implements them.

## Requirements

- Lives in `domain/port/in/` — no Spring or JPA annotations.
- Each interface declares exactly **one** method.
- Commands/Queries are `record` types nested inside the interface.
- Follow the naming and structure of existing use cases (e.g., `FollowUserUseCase`, `GetFollowersUseCase`).

## File Locations

```
backend/src/main/java/com/instagram/domain/port/in/
├── LikePostUseCase.java
├── UnlikePostUseCase.java
├── LikeCommentUseCase.java
├── UnlikeCommentUseCase.java
└── GetPostLikersUseCase.java
```

---

## Checklist

### `LikePostUseCase.java`

- [ ] Create interface:
  ```java
  public interface LikePostUseCase {
      void like(Command command);

      record Command(UUID postId, UUID userId) {}
  }
  ```

### `UnlikePostUseCase.java`

- [ ] Create interface:
  ```java
  public interface UnlikePostUseCase {
      void unlike(Command command);

      record Command(UUID postId, UUID userId) {}
  }
  ```

### `LikeCommentUseCase.java`

- [ ] Create interface:
  ```java
  public interface LikeCommentUseCase {
      void like(Command command);

      record Command(UUID commentId, UUID userId) {}
  }
  ```

### `UnlikeCommentUseCase.java`

- [ ] Create interface:
  ```java
  public interface UnlikeCommentUseCase {
      void unlike(Command command);

      record Command(UUID commentId, UUID userId) {}
  }
  ```

### `GetPostLikersUseCase.java`

- [ ] Create interface:
  ```java
  public interface GetPostLikersUseCase {
      Page<UserSummary> getLikers(Query query);

      record Query(UUID postId, UUID requestingUserId, int page, int size) {}
  }
  ```
  - Returns a `Page<UserSummary>` (from domain, not Spring's `Page` — use the same pattern as `GetFollowersUseCase`).
  - `requestingUserId` may be `null` for unauthenticated callers.

- [ ] Verify that all `Command` / `Query` field types are `UUID`, `int`, or domain types only.
