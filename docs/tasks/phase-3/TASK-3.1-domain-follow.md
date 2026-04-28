# TASK-3.1 — Domain Model: Follow

## Overview

Create the core `Follow` domain entity representing a directed follow relationship between two users. This is a pure Java class with **no framework dependencies**. It models the business concept of a follow link and supports both public (immediately accepted) and private (pending approval) follow flows.

## Requirements

- Must live in `domain/model/` — **no** `@Entity`, `@Component`, or Lombok annotations.
- Fields map directly to the `follows` table in `schema.sql`.
- Uses a hand-written **Builder** pattern (consistent with `User.java` and `Post.java`).
- Business behaviour lives inside the entity as copy-returning methods.
- Enum `FollowStatus` must be defined to represent the follow state.

## File Locations

```
backend/src/main/java/com/instagram/domain/model/Follow.java
backend/src/main/java/com/instagram/domain/model/FollowStatus.java
```

## Notes

- `id` is a surrogate UUID used for follow-request references (approve/decline endpoints).
- `followerId` and `followingId` are both `UUID` references — never `User` objects (pure domain).
- `status` defaults to `PENDING` for private accounts and `ACCEPTED` for public accounts — the service decides which to use at creation time.
- `withAccepted()` is a copy-returning method — never mutates state directly.
- Do **not** add `@Entity`, `@Table`, or any persistence annotations here.

## Checklist

- [x] Create `FollowStatus.java` enum with values:
  ```java
  public enum FollowStatus { PENDING, ACCEPTED }
  ```

- [x] Create `Follow.java` with fields:
  ```java
  private UUID id;
  private UUID followerId;
  private UUID followingId;
  private FollowStatus status;
  private java.time.Instant createdAt;
  ```

- [x] Implement private no-arg constructor (used by Builder only)

- [x] Implement static inner `Builder` class with:
  - [x] Setter-style methods returning `this` for each field
  - [x] `build()` method that validates required fields (`id`, `followerId`, `followingId`, `status`) and throws `IllegalStateException` if null

- [x] Implement a private `copy()` helper that returns a mutable copy via the Builder

- [x] Implement `withAccepted()`:
  ```java
  public Follow withAccepted() {
      return this.copy().status(FollowStatus.ACCEPTED).build();
  }
  ```

- [x] Add static factory method `of(UUID followerId, UUID followingId, FollowStatus status)`:
  ```java
  public static Follow of(UUID followerId, UUID followingId, FollowStatus status) {
      return new Builder()
          .id(UUID.randomUUID())
          .followerId(followerId)
          .followingId(followingId)
          .status(status)
          .createdAt(Instant.now())
          .build();
  }
  ```

- [x] Write unit test `FollowTest.java`:
  - [x] `builder_createsFollow_withRequiredFields()`
  - [x] `builder_throwsException_whenFollowerIdIsNull()`
  - [x] `withAccepted_returnsNewInstance_withAcceptedStatus()`
  - [x] `withAccepted_doesNotMutateOriginal()`
