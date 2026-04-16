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
- [ ] Create `domain/port/out/TokenPort.java`:
  ```java
  public interface TokenPort {
      String generateAccessToken(UUID userId, String role);
      String generateRefreshToken(UUID userId);
      Optional<UUID> validateAccessToken(String token);
      Optional<UUID> validateRefreshToken(String token);
  }
  ```

### UserService class
- [ ] Create `UserService.java` annotated with `@Service`
- [ ] Add constructor accepting `UserRepository`, `PasswordHashPort`, `TokenPort`, `EmailPort`
- [ ] Declare all fields `private final`
- [ ] Implement `RegisterUserUseCase`:
  - [ ] Check `existsByUsername` → throw `UserAlreadyExistsException("username", command.username())`
  - [ ] Check `existsByEmail` → throw `UserAlreadyExistsException("email", command.email())`
  - [ ] Hash password: `passwordHashPort.hash(command.password())`
  - [ ] Build `User` via Builder with `id = UUID.randomUUID()`, `status = ACTIVE`
  - [ ] Call `userRepository.save(user)` and return result
- [ ] Implement `LoginUseCase`:
  - [ ] Try `findByEmail` then `findByUsername` → `orElseThrow(InvalidCredentialsException::new)`
  - [ ] Check user `isActive()` → throw `InvalidCredentialsException` if not
  - [ ] Verify password: `passwordHashPort.verify(command.password(), user.passwordHash())` → throw `InvalidCredentialsException` on mismatch
  - [ ] Generate tokens via `tokenPort` → return `AuthResult`
- [ ] Implement `RefreshTokenUseCase`:
  - [ ] Validate refresh token → `orElseThrow(() -> new InvalidCredentialsException())`
  - [ ] Find user by extracted `userId` → generate new token pair → return `AuthResult`
- [ ] Implement `LogoutUseCase`:
  - [ ] Validate refresh token to extract `userId` (log warning if invalid, do not throw)
  - [ ] (Stub) — no session store in Phase 1; method is a no-op returning cleanly
- [ ] Implement `RequestPasswordResetUseCase`:
  - [ ] `findByEmail(command.email())` — if empty, return silently (no user enumeration)
  - [ ] Generate a random reset token (`UUID.randomUUID().toString()`)
  - [ ] Persist token in `password_reset_tokens` table (via a new `PasswordResetTokenRepository` out-port; stub as logged message if not implemented yet)
  - [ ] Call `emailPort.sendPasswordResetEmail(user.email(), token)``
- [ ] Implement `ConfirmPasswordResetUseCase`:
  - [ ] Load token record → if not found or expired → throw `PasswordResetTokenExpiredException`
  - [ ] Hash new password → find user → update `passwordHash` via `withUpdatedProfile` variant → save
  - [ ] Delete used token from DB
- [ ] Implement `GetUserProfileUseCase`:
  - [ ] `findByUsername(query.targetUsername())` → `orElseThrow(UserNotFoundException::withUsername)`
  - [ ] Load `UserStats` (stub `UserStats.zero(user.id())` until Phase 3)
  - [ ] Return `new UserProfile(user, stats, false)`
- [ ] Implement `UpdateProfileUseCase`:
  - [ ] `findById(command.userId())` → `orElseThrow`
  - [ ] Call `user.withUpdatedProfile(...)` → save → return updated user
