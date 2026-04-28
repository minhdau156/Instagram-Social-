# TASK-3.4 — In-Ports (Use-Case Interfaces)

## Overview

Define one use-case interface (in-port) per business operation in the social graph feature. Each interface has a single method and an inner `Command` or `Query` record for input data. These are pure Java interfaces with no Spring annotations.

## Requirements

- One file per use case in `domain/port/in/`.
- Each interface has **one** method with a descriptive name.
- Input is always an inner record (`Command` for writes, `Query` for reads).
- No framework dependencies in any of these files.

## File Locations

```
backend/src/main/java/com/instagram/domain/port/in/
├── FollowUserUseCase.java
├── UnfollowUserUseCase.java
├── GetFollowersUseCase.java
├── GetFollowingUseCase.java
├── ApproveFollowRequestUseCase.java
├── DeclineFollowRequestUseCase.java
└── GetFollowRequestsUseCase.java
```

## Checklist

### `FollowUserUseCase.java`
- [x] Create interface with:
  ```java
  public interface FollowUserUseCase {
      Follow follow(Command command);
      record Command(UUID followerId, String targetUsername) {}
  }
  ```

### `UnfollowUserUseCase.java`
- [x] Create interface with:
  ```java
  public interface UnfollowUserUseCase {
      void unfollow(Command command);
      record Command(UUID followerId, String targetUsername) {}
  }
  ```

### `GetFollowersUseCase.java`
- [x] Create interface with:
  ```java
  public interface GetFollowersUseCase {
      List<UserSummary> getFollowers(Query query);
      record Query(String targetUsername, UUID currentUserId, int page, int size) {}
  }
  ```
  > `UserSummary` is a value object defined in TASK-3.11 (or a domain record in `domain/model/`).

### `GetFollowingUseCase.java`
- [x] Create interface with:
  ```java
  public interface GetFollowingUseCase {
      List<UserSummary> getFollowing(Query query);
      record Query(String targetUsername, UUID currentUserId, int page, int size) {}
  }
  ```

### `ApproveFollowRequestUseCase.java`
- [x] Create interface with:
  ```java
  public interface ApproveFollowRequestUseCase {
      Follow approve(Command command);
      record Command(UUID followingId, UUID followRequestId) {}
  }
  ```

### `DeclineFollowRequestUseCase.java`
- [x] Create interface with:
  ```java
  public interface DeclineFollowRequestUseCase {
      void decline(Command command);
      record Command(UUID followingId, UUID followRequestId) {}
  }
  ```

### `GetFollowRequestsUseCase.java`
- [x] Create interface with:
  ```java
  public interface GetFollowRequestsUseCase {
      List<Follow> getFollowRequests(Query query);
      record Query(UUID userId) {}
  }
  ```

### Supporting value object
- [x] Create `domain/model/UserSummary.java` (if not already present from Phase 1):
  ```java
  public record UserSummary(
      UUID id,
      String username,
      String fullName,
      String avatarUrl,
      boolean isVerified,
      boolean isFollowing
  ) {}
  ```
