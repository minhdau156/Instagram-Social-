# TASK-1.2 — Domain Model: UserStats

## Overview

Create an immutable value object `UserStats` that represents the denormalized counters for a user's profile — post count, follower count, and following count. This is a lightweight read model used exclusively for profile display.

## Requirements

- Immutable — either a Java **record** or a hand-written value object with no setters.
- No framework annotations.
- Lives in `domain/model/` alongside `User.java`.

## File Location

```
backend/src/main/java/com/instagram/domain/model/UserStats.java
```

## Notes

- Backed by the `user_stats` table in the DB schema.
- The preferred implementation is a Java 21 **record** since all fields are read-only counters.
- A `zero(UUID userId)` static factory is useful for new accounts.
- This class is returned by `GetUserProfileUseCase` as part of the profile response.

## Checklist

- [ ] Create `UserStats.java` as a Java record:
  ```java
  public record UserStats(
      UUID userId,
      int postCount,
      int followerCount,
      int followingCount
  ) {}
  ```

- [ ] Add static factory `zero(UUID userId)`:
  ```java
  public static UserStats zero(UUID userId) {
      return new UserStats(userId, 0, 0, 0);
  }
  ```

- [ ] Write a minimal unit test `UserStatsTest.java`:
  - [ ] `zero_returnsAllCountsAsZero()`
  - [ ] Record equality: two `UserStats` with the same values are equal
