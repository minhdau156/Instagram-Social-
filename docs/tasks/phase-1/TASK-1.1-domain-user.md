# TASK-1.1 — Domain Model: User

## Overview

Create the core `User` domain entity — the heart of the authentication and user-management feature. This is a pure Java class with **no framework dependencies**. It models the business concept of a user account and enforces invariants via business methods.

## Requirements

- Must live in `domain/model/` — **no** `@Entity`, `@Component`, or Lombok annotations.
- Fields map directly to the `users` table in `schema.sql`.
- Uses a hand-written **Builder** pattern (see `Post.java` as the reference).
- Business behaviour lives inside the entity as copy-returning methods.
- Enum `UserStatus` must be defined (or co-located) to represent account state.

## File Location

```
backend/src/main/java/com/instagram/domain/model/User.java
backend/src/main/java/com/instagram/domain/model/UserStatus.java
```

## Notes

- `passwordHash` stores the BCrypt-hashed value — never the raw password.
- `isPrivate` defaults to `false`; `isVerified` defaults to `false`.
- `status` values: `ACTIVE`, `DEACTIVATED`, `SUSPENDED`.
- `withUpdatedProfile(...)` returns a new `User` with updated `fullName`, `bio`, `avatarUrl`, `isPrivate` — use the copy-builder idiom.
- `withDeactivated()` returns a copy with `status = DEACTIVATED`.
- Do **not** add `createdAt` / `updatedAt` — those are JPA auditing concerns handled in `BaseJpaEntity`.

## Checklist

- [x] Create `UserStatus.java` enum with values: `ACTIVE`, `DEACTIVATED`, `SUSPENDED`

- [x] Create `User.java` with fields:
  ```java
  private UUID id;
  private String username;
  private String email;
  private String phoneNumber;       // nullable
  private String passwordHash;      // nullable (OAuth2 users have none)
  private String fullName;
  private String bio;               // nullable
  private String avatarUrl;         // nullable
  private boolean isPrivate;
  private boolean isVerified;
  private UserStatus status;
  ```

- [x] Implement private no-arg constructor (used by Builder only)

- [x] Implement static inner `Builder` class with:
  - [x] Setter-style methods returning `this` for each field
  - [ ] `build()` method that validates required fields (`id`, `username`, `email`, `status`) and throws `IllegalStateException` if null

- [x] Implement a private `copy()` helper method that returns a mutable copy via the Builder

- [x] Implement `withUpdatedProfile(String fullName, String bio, String avatarUrl, boolean isPrivate)`:
  ```java
  public User withUpdatedProfile(String fullName, String bio, String avatarUrl, boolean isPrivate) {
      return this.copy()
          .fullName(fullName != null ? fullName : this.fullName)
          .bio(bio)
          .avatarUrl(avatarUrl)
          .isPrivate(isPrivate)
          .build();
  }
  ```

- [x] Implement `withDeactivated()`:
  ```java
  public User withDeactivated() {
      return this.copy().status(UserStatus.DEACTIVATED).build();
  }
  ```

- [x] Add `withAvatarUrl(String avatarUrl)` convenience method for avatar-only updates

- [x] Add `isActive()` convenience predicate: `return this.status == UserStatus.ACTIVE;`

- [ ] Write unit test `UserTest.java` in `domain/model/`:
  - [x] `builder_createsUser_withRequiredFields()`
  - [ ] `builder_throwsException_whenIdIsNull()`
  - [x] `withDeactivated_setsStatus_toDeactivated()`
  - [x] `withUpdatedProfile_returnsNewInstance_doesNotMutateOriginal()`
