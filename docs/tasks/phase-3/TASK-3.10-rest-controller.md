# TASK-3.10 — REST Controller: FollowController

## Overview

Implement `FollowController` — the HTTP entry point for all follow/unfollow and follow-request operations. Controllers are thin adapters: they validate input, delegate to use-case interfaces, and return structured `ResponseEntity` responses. No business logic lives here.

## Requirements

- No business logic in the controller — delegate entirely to in-port use-case interfaces.
- Extract current user's ID from `SecurityContextHolder` via a helper method.
- All endpoints that require authentication must be annotated with `@PreAuthorize("isAuthenticated()")`.
- Return explicit HTTP status codes via `ResponseEntity<T>`.
- Wrap all responses in `ApiResponse<T>`.

## File Location

```
backend/src/main/java/com/instagram/adapter/in/web/FollowController.java
```

## Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/users/{username}/follow` | Required | Follow a user |
| `DELETE` | `/api/v1/users/{username}/follow` | Required | Unfollow a user |
| `GET` | `/api/v1/users/{username}/followers` | Optional | List followers (paginated) |
| `GET` | `/api/v1/users/{username}/following` | Optional | List following (paginated) |
| `GET` | `/api/v1/follow-requests` | Required | Get pending follow requests |
| `POST` | `/api/v1/follow-requests/{id}/approve` | Required | Approve a follow request |
| `DELETE` | `/api/v1/follow-requests/{id}` | Required | Decline a follow request |

## Checklist

- [x] Annotate with `@RestController`, `@RequiredArgsConstructor`, `@Tag(name = "Follow")`
- [x] Inject via constructor: `FollowUserUseCase`, `UnfollowUserUseCase`, `GetFollowersUseCase`, `GetFollowingUseCase`, `GetFollowRequestsUseCase`, `ApproveFollowRequestUseCase`, `DeclineFollowRequestUseCase`
- [x] Add private helper:
  ```java
  private UUID currentUserId() {
      return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
  ```

- [x] Implement `POST /api/v1/users/{username}/follow`:
  ```java
  @PostMapping("/api/v1/users/{username}/follow")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ApiResponse<FollowResponse>> followUser(@PathVariable String username) {
      Follow follow = followUserUseCase.follow(new FollowUserUseCase.Command(currentUserId(), username));
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(FollowResponse.from(follow)));
  }
  ```

- [x] Implement `DELETE /api/v1/users/{username}/follow`:
  - Calls `unfollowUserUseCase.unfollow(new Command(currentUserId(), username))`
  - Returns `204 No Content`

- [x] Implement `GET /api/v1/users/{username}/followers`:
  - Query params: `page` (default 0), `size` (default 20)
  - Calls `getFollowersUseCase.getFollowers(new Query(username, currentUserIdOrNull(), page, size))`
  - Returns `200 OK` with `ApiResponse<List<UserSummaryResponse>>`

- [x] Implement `GET /api/v1/users/{username}/following`:
  - Same pattern as followers endpoint

- [x] Implement `GET /api/v1/follow-requests`:
  - `@PreAuthorize("isAuthenticated()")`
  - Calls `getFollowRequestsUseCase.getFollowRequests(new Query(currentUserId()))`
  - Returns `200 OK` with `ApiResponse<List<FollowResponse>>`

- [x] Implement `POST /api/v1/follow-requests/{id}/approve`:
  - `@PreAuthorize("isAuthenticated()")`
  - Calls `approveFollowRequestUseCase.approve(new Command(currentUserId(), id))`
  - Returns `200 OK` with updated `FollowResponse`

- [x] Implement `DELETE /api/v1/follow-requests/{id}`:
  - `@PreAuthorize("isAuthenticated()")`
  - Calls `declineFollowRequestUseCase.decline(new Command(currentUserId(), id))`
  - Returns `204 No Content`

- [x] Add a `@Nullable`-safe helper `currentUserIdOrNull()` that returns `null` when not authenticated (for public follower/following lists)
