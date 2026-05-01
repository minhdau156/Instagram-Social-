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

- [ ] Create `PostLike.java` with fields:
  ```java
  private UUID postId;
  private UUID userId;
  private Instant createdAt;
  ```

- [ ] Implement private no-arg constructor (used by Builder only)

- [ ] Implement static inner `Builder` class with:
  - [ ] Fluent setter methods returning `this` for each field
  - [ ] `build()` that validates `postId` and `userId` are not null, throws `IllegalStateException` otherwise

- [ ] Add static factory method `of(UUID postId, UUID userId)`:
  ```java
  public static PostLike of(UUID postId, UUID userId) {
      return new Builder()
          .postId(postId)
          .userId(userId)
          .createdAt(Instant.now())
          .build();
  }
  ```

- [ ] Implement public getters for all fields (no setters)

---

### `CommentLike.java`

- [ ] Create `CommentLike.java` with fields:
  ```java
  private UUID commentId;
  private UUID userId;
  private Instant createdAt;
  ```

- [ ] Same Builder pattern and static factory `of(UUID commentId, UUID userId)` as `PostLike`

- [ ] Implement public getters for all fields (no setters)

---

### Unit Tests

- [ ] Write `PostLikeTest.java`:
  - [ ] `of_createsPostLike_withCorrectFields()`
  - [ ] `builder_throwsException_whenPostIdIsNull()`
  - [ ] `builder_throwsException_whenUserIdIsNull()`

- [ ] Write `CommentLikeTest.java`:
  - [ ] `of_createsCommentLike_withCorrectFields()`
  - [ ] `builder_throwsException_whenCommentIdIsNull()`
