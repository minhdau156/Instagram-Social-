# TASK-4.8 — REST Controller: LikeController

## Overview

Implement `LikeController` — the HTTP entry point for all like/unlike operations on posts and comments. Controllers are thin adapters: they validate input, extract the current user, delegate to use-case interfaces, and return structured `ResponseEntity` responses. No business logic lives here.

## Requirements

- No business logic — delegate entirely to in-port use-case interfaces.
- Extract current user's ID from `SecurityContextHolder` via a private helper `currentUserId()`.
- All mutating endpoints (`POST`, `DELETE`) must be authenticated (`@PreAuthorize("isAuthenticated()")`).
- `GET` endpoints (likers list) may be public.
- Return explicit HTTP status codes via `ResponseEntity<T>`.
- Wrap all responses in `ApiResponse<T>`.

## File Location

```
backend/src/main/java/com/instagram/adapter/in/web/LikeController.java
```

## Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/posts/{id}/like` | Required | Like a post |
| `DELETE` | `/api/v1/posts/{id}/like` | Required | Unlike a post |
| `GET` | `/api/v1/posts/{id}/likers` | Optional | Get paginated list of users who liked a post |
| `POST` | `/api/v1/comments/{id}/like` | Required | Like a comment |
| `DELETE` | `/api/v1/comments/{id}/like` | Required | Unlike a comment |

---

## Checklist

- [x] Annotate with `@RestController`, `@RequiredArgsConstructor`, `@Tag(name = "Likes")`
- [x] Inject via constructor: `LikePostUseCase`, `UnlikePostUseCase`, `LikeCommentUseCase`, `UnlikeCommentUseCase`, `GetPostLikersUseCase`
- [x] Add private helper:
  ```java
  private UUID currentUserId() {
      return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
  ```

- [x] Implement `POST /api/v1/posts/{id}/like`:
  ```java
  @PostMapping("/api/v1/posts/{id}/like")
  @PreAuthorize("isAuthenticated()")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void likePost(@PathVariable UUID id) {
      likePostUseCase.like(new LikePostUseCase.Command(id, currentUserId()));
  }
  ```
  - Returns `204 No Content` on success.

- [x] Implement `DELETE /api/v1/posts/{id}/like`:
  - Calls `unlikePostUseCase.unlike(new Command(id, currentUserId()))`
  - Returns `204 No Content`

- [x] Implement `GET /api/v1/posts/{id}/likers`:
  - Query params: `page` (default 0), `size` (default 20)
  - Calls `getPostLikersUseCase.getPostLikers(new Query(id, currentUserIdOrNull(), page, size))`
  - Returns `200 OK` with `ApiResponse<List<UserSummaryResponse>>`

- [x] Implement `POST /api/v1/comments/{id}/like`:
  - Calls `likeCommentUseCase.likeComment(new LikeCommentUseCase.Command(currentUserId(), id))`
  - Returns `204 No Content`

- [x] Implement `DELETE /api/v1/comments/{id}/like`:
  - Calls `unlikeCommentUseCase.unlikeComment(new UnlikeCommentUseCase.Command(currentUserId(), id))`
  - Returns `204 No Content`

- [x] Add a `@Nullable`-safe `currentUserIdOrNull()` helper for the public likers endpoint:
  ```java
  @Nullable
  private UUID currentUserIdOrNull() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
          return null;
      }
      return (UUID) auth.getPrincipal();
  }
  ```
