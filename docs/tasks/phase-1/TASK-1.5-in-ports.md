# TASK-1.5 — In-Ports (Use-Case Interfaces)

## Overview

Define one use-case interface (in-port) per business operation. Each interface has a single method and an inner `Command` or `Query` record for input data. These are pure Java interfaces — no Spring annotations.

## Requirements

- One file per use case in `domain/port/in/`.
- Each interface has **one** method with a descriptive name.
- Input is always an inner record (`Command` for writes, `Query` for reads).
- No framework dependencies in any of these files.

## File Locations

```
backend/src/main/java/com/instagram/domain/port/in/
├── RegisterUserUseCase.java
├── LoginUseCase.java
├── RefreshTokenUseCase.java
├── LogoutUseCase.java
├── RequestPasswordResetUseCase.java
├── ConfirmPasswordResetUseCase.java
├── GetUserProfileUseCase.java
└── UpdateProfileUseCase.java
```

## Notes

- `AuthResult` is a value object returned by `LoginUseCase` and `RefreshTokenUseCase`. Define it as a record in `domain/model/AuthResult.java` (access token, refresh token, expiry seconds).
- `UserProfile` is a value object returned by `GetUserProfileUseCase`. Define it as a record in `domain/model/UserProfile.java` (wraps `User` + `UserStats`).
- `identifier` in `LoginUseCase.Command` can be either username or email — the service resolves which.

## Checklist

### `RegisterUserUseCase.java`
- [ ] Create interface with:
  ```java
  public interface RegisterUserUseCase {
      User register(Command command);
      record Command(String username, String email, String password, String fullName) {}
  }
  ```

### `LoginUseCase.java`
- [ ] Create interface with:
  ```java
  public interface LoginUseCase {
      AuthResult login(Command command);
      record Command(String identifier, String password) {}
  }
  ```

### `RefreshTokenUseCase.java`
- [ ] Create interface with:
  ```java
  public interface RefreshTokenUseCase {
      AuthResult refresh(Command command);
      record Command(String refreshToken) {}
  }
  ```

### `LogoutUseCase.java`
- [ ] Create interface with:
  ```java
  public interface LogoutUseCase {
      void logout(Command command);
      record Command(String refreshToken) {}
  }
  ```

### `RequestPasswordResetUseCase.java`
- [ ] Create interface with:
  ```java
  public interface RequestPasswordResetUseCase {
      void requestReset(Command command);
      record Command(String email) {}
  }
  ```

### `ConfirmPasswordResetUseCase.java`
- [ ] Create interface with:
  ```java
  public interface ConfirmPasswordResetUseCase {
      void confirmReset(Command command);
      record Command(String token, String newPassword) {}
  }
  ```

### `GetUserProfileUseCase.java`
- [ ] Create interface with:
  ```java
  public interface GetUserProfileUseCase {
      UserProfile getProfile(Query query);
      record Query(String targetUsername, UUID currentUserId) {}
  }
  ```

### `UpdateProfileUseCase.java`
- [ ] Create interface with:
  ```java
  public interface UpdateProfileUseCase {
      User updateProfile(Command command);
      record Command(UUID userId, String fullName, String bio, boolean isPrivate) {}
  }
  ```

### Supporting value objects
- [ ] Create `domain/model/AuthResult.java`:
  ```java
  public record AuthResult(String accessToken, String refreshToken, long expiresIn) {}
  ```
- [ ] Create `domain/model/UserProfile.java`:
  ```java
  public record UserProfile(User user, UserStats stats, boolean isFollowing) {}
  ```
