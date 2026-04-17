# TASK-1.6 — Domain Service: UserService

## Overview

Implement `UserService` — the central domain service that orchestrates all auth and user-management use cases. It depends only on **out-port interfaces** (never on JPA or Spring infrastructure directly) and implements all in-port interfaces from TASK-1.5.

## Requirements

- Annotated with `@Service` (Spring manages the bean, but the domain logic stays pure).
- Constructor injection only — all dependencies are `final`.
- Delegates password hashing to `PasswordHashPort` (never calls BCrypt directly).
- Delegates token generation/validation to `TokenPort`.
- Delegates email sending to `EmailPort`.
- Throws domain exceptions (TASK-1.3) on business rule violations — never raw `RuntimeException`.

## File Location

```
backend/src/main/java/com/instagram/domain/service/UserService.java
```

## Dependencies (all are out-port interfaces)

| Field | Interface | Purpose |
|-------|-----------|---------| 
| `userRepository` | `UserRepository` | Persist / retrieve users |
| `passwordHashPort` | `PasswordHashPort` | Hash & verify passwords |
| `tokenPort` | `TokenPort` | Generate & validate JWT pairs |
| `emailPort` | `EmailPort` | Send password-reset emails |

> **Note:** `PasswordHashPort` and `EmailPort` are defined in TASK-1.12. `TokenPort` is a new out-port you must define alongside this task.

## Notes

- `register()`: check existence by username and email → hash password → build `User` → save.
- `login()`: find user by identifier (try email, then username) → verify password → generate tokens.
- `refresh()`: validate refresh token via `TokenPort` → issue new token pair.
- `logout()`: invalidate the refresh token (mark as used / delete from `user_sessions` if tracked).
- `requestReset()`: look up user by email (silently ignore if not found — no user enumeration) → generate a short-lived reset token → save to DB → send email via `EmailPort`.
- `confirmReset()`: look up token → check expiry → hash new password → update user → delete token.
- `getProfile()`: find user → fetch stats → check if `currentUserId` follows `targetUsername` (stub `isFollowing = false` until Phase 3).
- `updateProfile()`: find user by ID → call `withUpdatedProfile(...)` → save.

## Checklist

### TokenPort out-port
- [x] Create `domain/port/out/TokenPort.java`:
  ```java
  public interface TokenPort {
      String generateAccessToken(UUID userId, String role);
      String generateRefreshToken(UUID userId);
      Optional<UUID> validateAccessToken(String token);
      Optional<UUID> validateRefreshToken(String token);
  }
  ```

### UserService class
- [x] Create `UserService.java` annotated with `@Service`
- [x] Add constructor accepting `UserRepository`, `PasswordHashPort`, `TokenPort`, `EmailPort`
- [x] Declare all fields `private final`
- [x] Implement `RegisterUserUseCase`:
  - [x] Check `existsByUsername` → throw `UserAlreadyExistsException("username", command.username())`
  - [x] Check `existsByEmail` → throw `UserAlreadyExistsException("email", command.email())`
  - [x] Hash password: `passwordHashPort.hash(command.password())`
  - [x] Build `User` via Builder with `id = UUID.randomUUID()`, `status = ACTIVE`
  - [x] Call `userRepository.save(user)` and return result
- [x] Implement `LoginUseCase`:
  - [x] Try `findByEmail` then `findByUsername` → `orElseThrow(InvalidCredentialsException::new)`
  - [x] Check user `isActive()` → throw `InvalidCredentialsException` if not
  - [x] Verify password: `passwordHashPort.verify(command.password(), user.passwordHash())` → throw `InvalidCredentialsException` on mismatch
  - [x] Generate tokens via `tokenPort` → return `AuthResult`
- [x] Implement `RefreshTokenUseCase`:
  - [x] Validate refresh token → `orElseThrow(() -> new InvalidCredentialsException())`
  - [x] Find user by extracted `userId` → generate new token pair → return `AuthResult`
- [x] Implement `LogoutUseCase`:
  - [x] Validate refresh token to extract `userId` (log warning if invalid, do not throw)
  - [x] (Stub) — no session store in Phase 1; method is a no-op returning cleanly
- [x] Implement `RequestPasswordResetUseCase`:
  - [x] `findByEmail(command.email())` — if empty, return silently (no user enumeration)
  - [x] Generate a random reset token (`UUID.randomUUID().toString()`)
  - [x] Persist token in `password_reset_tokens` table (via a new `PasswordResetTokenRepository` out-port; stub as logged message if not implemented yet)
  - [x] Call `emailPort.sendPasswordResetEmail(user.email(), token)`
- [x] Implement `ConfirmPasswordResetUseCase`:
  - [x] Load token record → if not found or expired → throw `PasswordResetTokenExpiredException`
  - [x] Hash new password → find user → update `passwordHash` via `withUpdatedProfile` variant → save
  - [x] Delete used token from DB
- [x] Implement `GetUserProfileUseCase`:
  - [x] `findByUsername(query.targetUsername())` → `orElseThrow(UserNotFoundException::withUsername)`
  - [x] Load `UserStats` (stub `UserStats.zero(user.id())` until Phase 3)
  - [x] Return `new UserProfile(user, stats, false)`
- [x] Implement `UpdateProfileUseCase`:
  - [x] `findById(command.userId())` → `orElseThrow`
  - [x] Call `user.withUpdatedProfile(...)` → save → return updated user
