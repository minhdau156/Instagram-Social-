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

- [x] Create `UserNotFoundException.java`
- [x] Create `UserAlreadyExistsException.java`
- [x] Create `InvalidCredentialsException.java`
- [x] Create `PasswordResetTokenExpiredException.java`
- [x] Verify no `@Component` / `@Service` or any Spring annotations appear in any exception class
