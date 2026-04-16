# TASK-1.3 — Domain Exceptions

## Overview

Create all domain-level exceptions for the auth/user feature. These are pure Java classes that represent business rule violations. They are thrown by the domain service (`UserService`) and mapped to HTTP status codes by `GlobalExceptionHandler`.

## Requirements

- All exceptions live in `domain/exception/` — no framework annotations.
- All extend `RuntimeException` (unchecked, consistent with the rest of the project).
- Each must accept a descriptive message.
- `GlobalExceptionHandler` will map them in TASK-1.16.

## File Locations

```
backend/src/main/java/com/instagram/domain/exception/
├── UserNotFoundException.java
├── UserAlreadyExistsException.java
├── InvalidCredentialsException.java
└── PasswordResetTokenExpiredException.java
```

## Notes

- These exceptions carry only a `message`. No extra fields needed at this stage.
- Messages should be user-safe (no stack traces, no internal IDs in the default message).
- Follow the same pattern used for any existing domain exceptions in the codebase.

## Checklist

- [ ] Create `UserNotFoundException.java`:
  ```java
  public class UserNotFoundException extends RuntimeException {
      public UserNotFoundException(String message) { super(message); }
      // convenience factory
      public static UserNotFoundException withUsername(String username) {
          return new UserNotFoundException("User not found: " + username);
      }
      public static UserNotFoundException withId(UUID id) {
          return new UserNotFoundException("User not found with id: " + id);
      }
  }
  ```

- [ ] Create `UserAlreadyExistsException.java`:
  ```java
  public class UserAlreadyExistsException extends RuntimeException {
      public UserAlreadyExistsException(String field, String value) {
          super("A user already exists with " + field + ": " + value);
      }
  }
  ```

- [ ] Create `InvalidCredentialsException.java`:
  ```java
  public class InvalidCredentialsException extends RuntimeException {
      public InvalidCredentialsException() {
          super("Invalid username or password");
      }
  }
  ```

- [ ] Create `PasswordResetTokenExpiredException.java`:
  ```java
  public class PasswordResetTokenExpiredException extends RuntimeException {
      public PasswordResetTokenExpiredException() {
          super("Password reset token has expired or is invalid");
      }
  }
  ```

- [ ] Verify no `@Component` / `@Service` or any Spring annotations appear in any exception class
