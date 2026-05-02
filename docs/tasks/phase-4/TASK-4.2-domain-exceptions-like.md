# TASK-4.2 — Domain Exceptions: Like

## Overview

Create domain-layer exceptions that represent business rule violations for the Like feature. These are pure Java exceptions with no framework dependencies. They are thrown by `LikeService` and caught by the `GlobalExceptionHandler` to produce appropriate HTTP error responses.

## Requirements

- Must live in `domain/exception/` — no Spring annotations.
- Extend `RuntimeException`.
- Provide a clear, user-facing message in the constructor.
- Follow the naming and structure of existing exceptions (e.g., `AlreadyFollowingException`, `FollowRequestNotFoundException`).

## File Locations

```
backend/src/main/java/com/instagram/domain/exception/AlreadyLikedException.java
backend/src/main/java/com/instagram/domain/exception/NotLikedException.java
```

## Notes

- `AlreadyLikedException` is thrown when a user tries to like a post or comment they have already liked.
- `NotLikedException` is thrown when a user tries to unlike a post or comment they have not liked.
- The `GlobalExceptionHandler` (already exists from Phase 3) must be extended in a later task to map these to HTTP responses (`409 Conflict` for `AlreadyLikedException`, `404 Not Found` for `NotLikedException`).

---

## Checklist

### `AlreadyLikedException.java`

- [x] Create `AlreadyLikedException extends RuntimeException`:
  ```java
  public class AlreadyLikedException extends RuntimeException {
      public AlreadyLikedException(String targetType, UUID targetId) {
          super(String.format("User has already liked %s '%s'", targetType, targetId));
      }
  }
  ```
  - `targetType` is `"post"` or `"comment"`.

### `NotLikedException.java`

- [x] Create `NotLikedException extends RuntimeException`:
  ```java
  public class NotLikedException extends RuntimeException {
      public NotLikedException(String targetType, UUID targetId) {
          super(String.format("User has not liked %s '%s'", targetType, targetId));
      }
  }
  ```

### `GlobalExceptionHandler` Update

- [x] In the existing `GlobalExceptionHandler.java`, add:
  ```java
  @ExceptionHandler(AlreadyLikedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAlreadyLiked(AlreadyLikedException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(ApiResponse.error(ex.getMessage()));
  }

  @ExceptionHandler(NotLikedException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotLiked(NotLikedException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(ex.getMessage()));
  }
  ```
