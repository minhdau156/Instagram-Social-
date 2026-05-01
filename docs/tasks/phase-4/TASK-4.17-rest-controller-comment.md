# TASK-4.17 — REST Controller: CommentController

## Overview

Implement `CommentController` — the HTTP entry point for all comment operations. Controllers are thin adapters: they validate input, delegate to use-case interfaces, and return structured `ResponseEntity` responses. No business logic lives here.

## Requirements

- No business logic — delegate entirely to in-port use-case interfaces.
- Extract current user's ID from `SecurityContextHolder`.
- All mutating endpoints (`POST`, `PUT`, `DELETE`) must be authenticated.
- `GET` endpoints (listing comments, replies) are public.
- Return explicit HTTP status codes via `ResponseEntity<T>`.
- Wrap all responses in `ApiResponse<T>`.

## File Location

```
backend/src/main/java/com/instagram/adapter/in/web/CommentController.java
```

## Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/api/v1/posts/{id}/comments` | Optional | List top-level comments (paginated) |
| `POST` | `/api/v1/posts/{id}/comments` | Required | Add a comment |
| `GET` | `/api/v1/comments/{id}/replies` | Optional | List replies for a comment (paginated) |
| `PUT` | `/api/v1/comments/{id}` | Required | Edit a comment |
| `DELETE` | `/api/v1/comments/{id}` | Required | Soft-delete a comment |

---

## Checklist

- [ ] Annotate with `@RestController`, `@RequiredArgsConstructor`, `@Tag(name = "Comments")`
- [ ] Inject via constructor: `AddCommentUseCase`, `EditCommentUseCase`, `DeleteCommentUseCase`, `GetCommentsUseCase`, `GetRepliesUseCase`
- [ ] Add private helper `currentUserId()` and `currentUserIdOrNull()`

- [ ] Implement `GET /api/v1/posts/{id}/comments`:
  ```java
  @GetMapping("/api/v1/posts/{id}/comments")
  public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
          @PathVariable UUID id,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size) {
      Page<Comment> comments = getCommentsUseCase.getComments(
          new GetCommentsUseCase.Query(id, page, size));
      return ResponseEntity.ok(ApiResponse.success(
          comments.map(CommentResponse::from)));
  }
  ```

- [ ] Implement `POST /api/v1/posts/{id}/comments`:
  - `@PreAuthorize("isAuthenticated()")`
  - Request body: `@Valid @RequestBody AddCommentRequest`
  - Calls `addCommentUseCase.addComment(new Command(id, currentUserId(), request.content(), request.parentId()))`
  - Returns `201 Created` with `CommentResponse`

- [ ] Implement `GET /api/v1/comments/{id}/replies`:
  - No auth required
  - Query params: `page` (default 0), `size` (default 10)
  - Returns `200 OK` with `Page<CommentResponse>`

- [ ] Implement `PUT /api/v1/comments/{id}`:
  - `@PreAuthorize("isAuthenticated()")`
  - Request body: `@Valid @RequestBody EditCommentRequest`
  - Calls `editCommentUseCase.editComment(new Command(id, currentUserId(), request.content()))`
  - Returns `200 OK` with updated `CommentResponse`

- [ ] Implement `DELETE /api/v1/comments/{id}`:
  - `@PreAuthorize("isAuthenticated()")`
  - Calls `deleteCommentUseCase.deleteComment(new Command(id, currentUserId()))`
  - Returns `204 No Content`

- [ ] Add Swagger `@Operation` annotations on each endpoint
