# TASK-4.1 — Domain Models: PostLike & CommentLike

## Overview

Create the core domain value objects representing a "like" interaction on a post and on a comment. These are pure Java classes with **no framework dependencies**. They model the business concept of a like relationship between a user and a piece of content.

## Requirements

- Must live in `domain/model/` — **no** `@Entity`, `@Component`, or Lombok annotations.
- Fields map directly to the `post_likes` and `comment_likes` tables in `schema.sql`.
- Use a hand-written **Builder** pattern (consistent with `Follow.java`, `Post.java`).
- Immutable after creation — no setters.

## File Locations

```
backend/src/main/java/com/instagram/domain/model/PostLike.java
backend/src/main/java/com/instagram/domain/model/CommentLike.java
```

## Notes

- `postId` / `commentId` and `userId` are `UUID` references — never entity objects (pure domain).
- `createdAt` is an `Instant` set at creation time via `Instant.now()`.
- There is no surrogate `id` — the composite (`postId`/`commentId` + `userId`) is the natural key.
- Do **not** add `@Entity`, `@Table`, or any persistence annotations here.

---

## Checklist

### `PostLike.java`

- [x] Create `PostLike.java` with fields:
  ```java
  private UUID postId;
  private UUID userId;
  private Instant createdAt;
  ```

- [x] Implement private no-arg constructor (used by Builder only)

- [x] Implement static inner `Builder` class with:
  - [x] Fluent setter methods returning `this` for each field
  - [x] `build()` that validates `postId` and `userId` are not null, throws `IllegalStateException` otherwise

- [x] Add static factory method `of(UUID postId, UUID userId)`:
  ```java
  public static PostLike of(UUID postId, UUID userId) {
      return new Builder()
          .postId(postId)
          .userId(userId)
          .createdAt(Instant.now())
          .build();
  }
  ```

- [x] Implement public getters for all fields (no setters)

---

### `CommentLike.java`

- [x] Create `CommentLike.java` with fields:
  ```java
  private UUID commentId;
  private UUID userId;
  private Instant createdAt;
  ```

- [x] Same Builder pattern and static factory `of(UUID commentId, UUID userId)` as `PostLike`

- [x] Implement public getters for all fields (no setters)

---

### Unit Tests

- [x] Write `PostLikeTest.java`:
  - [x] `of_createsPostLike_withCorrectFields()`
  - [x] `builder_throwsException_whenPostIdIsNull()`
  - [x] `builder_throwsException_whenUserIdIsNull()`

- [x] Write `CommentLikeTest.java`:
  - [x] `of_createsCommentLike_withCorrectFields()`
  - [x] `builder_throwsException_whenCommentIdIsNull()`
