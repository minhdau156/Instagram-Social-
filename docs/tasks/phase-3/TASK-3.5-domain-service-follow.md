# TASK-3.5 — Domain Service: FollowService

## Overview

Implement `FollowService` — the domain service that orchestrates all social-graph use cases. It depends only on **out-port interfaces** (`FollowRepository`, `UserRepository`) and implements all in-ports from TASK-3.4. Business rules for public vs. private accounts live here.

## Requirements

- Annotated with `@Service`.
- Constructor injection only — all dependencies are `final`.
- Never calls JPA repositories directly — only through out-port interfaces.
- Throws domain exceptions (TASK-3.2) on business rule violations.
- Stats counters (`follower_count`, `following_count`) are updated via `UserStatsRepository` (from TASK-3.9) within the same transaction.

## File Location

```
backend/src/main/java/com/instagram/domain/service/FollowService.java
```

## Dependencies (all are out-port interfaces)

| Field | Interface | Purpose |
|-------|-----------|---------| 
| `followRepository` | `FollowRepository` | Persist / query follow relationships |
| `userRepository` | `UserRepository` | Look up target user to check `isPrivate` |
| `userStatsRepository` | `UserStatsRepository` | Increment / decrement follower/following counts |

## Notes

- **Self-follow guard**: check `command.followerId().equals(target.id())` → throw `CannotFollowYourselfException`.
- **Duplicate guard**: check `followRepository.findByFollowerIdAndFollowingId(...)` → if present, throw `AlreadyFollowingException`.
- **Public account**: `follow()` creates `Follow` with `status = ACCEPTED` and immediately increments both counters.
- **Private account**: `follow()` creates `Follow` with `status = PENDING` — counters are **not** incremented yet.
- **approve()**: find the `PENDING` request → call `follow.withAccepted()` → save → increment both counters.
- **decline()**: find the `PENDING` request → call `followRepository.delete(...)` — no counter change.
- **unfollow()**: find the existing accepted follow → delete it → decrement both counters.
- `getFollowers()` / `getFollowing()`: delegate to `followRepository`, then map each `Follow` to a `UserSummary` by fetching the user record and checking the `isFollowing` flag for the current user.

## Checklist

- [x] Create `FollowService.java` annotated with `@Service`
- [x] Add constructor accepting `FollowRepository`, `UserRepository`, `UserStatsRepository`
- [x] Declare all fields `private final`

- [x] Implement `FollowUserUseCase`:
  - [x] Resolve target user: `userRepository.findByUsername(command.targetUsername())` → `orElseThrow(UserNotFoundException::withUsername)`
  - [x] Guard: `command.followerId().equals(target.id())` → throw `CannotFollowYourselfException`
  - [x] Guard: existing relationship → throw `AlreadyFollowingException(target.username())`
  - [x] Choose status: `target.isPrivate() ? FollowStatus.PENDING : FollowStatus.ACCEPTED`
  - [x] Build and save `Follow.of(followerId, targetId, status)`
  - [x] If status is `ACCEPTED`: call `userStatsRepository.incrementFollowerCount(targetId)` and `userStatsRepository.incrementFollowingCount(followerId)`
  - [x] Return saved `Follow`

- [x] Implement `UnfollowUserUseCase`:
  - [x] Resolve target user by username
  - [x] Find existing relationship → `orElseThrow(FollowRequestNotFoundException::new)` if absent
  - [x] Call `followRepository.delete(followerId, targetId)`
  - [x] Decrement counters only if the deleted follow had `status = ACCEPTED`

- [x] Implement `ApproveFollowRequestUseCase`:
  - [x] Load follow by `command.followRequestId()` via `followRepository.findByFollowerIdAndFollowingId` or a dedicated `findById`
  - [x] Verify `follow.followingId().equals(command.followingId())` → throw `FollowRequestNotFoundException` if mismatch
  - [x] Call `follow.withAccepted()` → save
  - [x] Increment follower/following counters

- [x] Implement `DeclineFollowRequestUseCase`:
  - [x] Same lookup and ownership check as approve
  - [x] Call `followRepository.delete(follow.followerId(), follow.followingId())`

- [x] Implement `GetFollowRequestsUseCase`:
  - [x] Return `followRepository.findPendingRequestsByFollowingId(query.userId())`

- [x] Implement `GetFollowersUseCase`:
  - [x] Resolve target user by username
  - [x] Build `Pageable` from `query.page()` / `query.size()`
  - [x] Fetch follows, map each `follow.followerId()` to a `UserSummary`

- [x] Implement `GetFollowingUseCase`:
  - [x] Same pattern as `GetFollowersUseCase` using `follow.followingId()`
