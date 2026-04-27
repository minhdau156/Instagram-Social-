# TASK-3.12 — GlobalExceptionHandler — Follow Mappings

## Overview

Register HTTP error mappings for the three domain exceptions introduced in TASK-3.2. These are added to the existing `GlobalExceptionHandler` class — do **not** create a new handler class.

## Requirements

- Extend the existing `GlobalExceptionHandler` in `adapter/in/web/` (or `infrastructure/`).
- Each `@ExceptionHandler` method must return a consistent `ApiResponse<Void>` error body.
- Use appropriate HTTP status codes as per REST conventions.

## File Location

```
backend/src/main/java/com/instagram/adapter/in/web/GlobalExceptionHandler.java
  (or wherever the existing handler lives — check the project)
```

## HTTP Mappings

| Exception | HTTP Status | Reason |
|-----------|-------------|--------|
| `AlreadyFollowingException` | `409 Conflict` | User is already followed or has a pending request |
| `FollowRequestNotFoundException` | `404 Not Found` | The follow request ID does not exist or is not owned by the caller |
| `CannotFollowYourselfException` | `400 Bad Request` | Self-follow is a client error |

## Checklist

- [ ] Open the existing `GlobalExceptionHandler.java`
- [ ] Add handler for `AlreadyFollowingException`:
  ```java
  @ExceptionHandler(AlreadyFollowingException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiResponse<Void> handleAlreadyFollowing(AlreadyFollowingException ex) {
      return ApiResponse.error(ex.getMessage());
  }
  ```

- [ ] Add handler for `FollowRequestNotFoundException`:
  ```java
  @ExceptionHandler(FollowRequestNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponse<Void> handleFollowRequestNotFound(FollowRequestNotFoundException ex) {
      return ApiResponse.error(ex.getMessage());
  }
  ```

- [ ] Add handler for `CannotFollowYourselfException`:
  ```java
  @ExceptionHandler(CannotFollowYourselfException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleCannotFollowYourself(CannotFollowYourselfException ex) {
      return ApiResponse.error(ex.getMessage());
  }
  ```

- [ ] Verify that each new handler follows the same response format as the existing handlers in the file
- [ ] Run existing exception handler tests to confirm no regressions
