# TASK-1.16 — GlobalExceptionHandler: Auth Exception Mappings

## Overview

Extend the existing `GlobalExceptionHandler` (created in TASK-0.6) with `@ExceptionHandler` methods for all domain exceptions introduced in Phase 1. No new class is created — this is an **update to the existing handler**.

## Requirements

- Map each new domain exception to the correct HTTP status code.
- Use `log.warn(...)` for all 4xx errors (consistent with Phase 0 convention).
- Return `ApiResponse.error(e.getMessage())` — never expose internal details.

## File Location

```
backend/src/main/java/com/instagram/adapter/in/web/GlobalExceptionHandler.java
```
*(Existing file — add new @ExceptionHandler methods only)*

## Checklist

- [ ] Add handler for `UserNotFoundException`:
  ```java
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException e) {
      log.warn("User not found: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
  }
  ```
  → HTTP **404 Not Found**

- [ ] Add handler for `UserAlreadyExistsException`:
  ```java
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExists(UserAlreadyExistsException e) {
      log.warn("User already exists: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(e.getMessage()));
  }
  ```
  → HTTP **409 Conflict**

- [ ] Add handler for `InvalidCredentialsException`:
  ```java
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(InvalidCredentialsException e) {
      log.warn("Invalid credentials attempt");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
  }
  ```
  → HTTP **401 Unauthorized**

- [ ] Add handler for `PasswordResetTokenExpiredException`:
  ```java
  @ExceptionHandler(PasswordResetTokenExpiredException.class)
  public ResponseEntity<ApiResponse<Void>> handleTokenExpired(PasswordResetTokenExpiredException e) {
      log.warn("Password reset token expired");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
  }
  ```
  → HTTP **400 Bad Request**

- [ ] Verify the new handlers appear **before** the catch-all `@ExceptionHandler(Exception.class)` method

- [ ] Update `GlobalExceptionHandlerTest.java` (from TASK-0.6) to cover the new handlers:
  - [ ] `handleUserNotFound_returns404()`
  - [ ] `handleUserAlreadyExists_returns409()`
  - [ ] `handleInvalidCredentials_returns401()`
  - [ ] `handlePasswordResetTokenExpired_returns400()`
