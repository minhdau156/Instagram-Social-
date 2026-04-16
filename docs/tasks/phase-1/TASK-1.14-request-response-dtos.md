# TASK-1.14 — Request / Response DTOs

## Overview

Create all Java records that represent the request bodies and response shapes for the auth and user-management controllers. These live in the `adapter/in/web/dto/` package.

## Requirements

- All DTOs are Java **records** (Java 21).
- Request records carry Bean Validation annotations.
- Response records have a static factory `from(DomainObject)` method.
- No Lombok needed (records are already concise).
- No business logic — only data shaping.

## File Locations

```
backend/src/main/java/com/instagram/adapter/in/web/dto/
├── RegisterRequest.java
├── LoginRequest.java
├── RefreshRequest.java
├── PasswordResetRequest.java
├── PasswordResetConfirmRequest.java
├── UpdateProfileRequest.java
├── AuthResponse.java
├── UserResponse.java
└── UserProfileResponse.java
```

## Checklist

### Request DTOs

- [ ] Create `RegisterRequest.java`:
  ```java
  public record RegisterRequest(
      @NotBlank @Size(min = 3, max = 30) String username,
      @NotBlank @Email String email,
      @NotBlank @Size(min = 8, max = 72) String password,
      @NotBlank @Size(max = 100) String fullName
  ) {}
  ```

- [ ] Create `LoginRequest.java`:
  ```java
  public record LoginRequest(
      @NotBlank String identifier,    // email or username
      @NotBlank String password
  ) {}
  ```

- [ ] Create `RefreshRequest.java`:
  ```java
  public record RefreshRequest(
      @NotBlank String refreshToken
  ) {}
  ```

- [ ] Create `PasswordResetRequest.java`:
  ```java
  public record PasswordResetRequest(
      @NotBlank @Email String email
  ) {}
  ```

- [ ] Create `PasswordResetConfirmRequest.java`:
  ```java
  public record PasswordResetConfirmRequest(
      @NotBlank String token,
      @NotBlank @Size(min = 8, max = 72) String newPassword
  ) {}
  ```

- [ ] Create `UpdateProfileRequest.java`:
  ```java
  public record UpdateProfileRequest(
      @Size(max = 100) String fullName,
      @Size(max = 150) String bio,
      Boolean isPrivate
  ) {}
  ```

### Response DTOs

- [ ] Create `AuthResponse.java`:
  ```java
  public record AuthResponse(
      String accessToken,
      String refreshToken,
      long expiresIn    // seconds
  ) {
      public static AuthResponse from(AuthResult result) {
          return new AuthResponse(result.accessToken(), result.refreshToken(), result.expiresIn());
      }
  }
  ```

- [ ] Create `UserResponse.java`:
  ```java
  public record UserResponse(
      UUID id,
      String username,
      String email,
      String fullName,
      String bio,
      String avatarUrl,
      boolean isPrivate,
      boolean isVerified
  ) {
      public static UserResponse from(User user) {
          return new UserResponse(
              user.id(), user.username(), user.email(),
              user.fullName(), user.bio(), user.avatarUrl(),
              user.isPrivate(), user.isVerified()
          );
      }
  }
  ```

- [ ] Create `UserProfileResponse.java`:
  ```java
  public record UserProfileResponse(
      UserResponse user,
      int postCount,
      int followerCount,
      int followingCount,
      boolean isFollowing
  ) {
      public static UserProfileResponse from(UserProfile profile) {
          return new UserProfileResponse(
              UserResponse.from(profile.user()),
              profile.stats().postCount(),
              profile.stats().followerCount(),
              profile.stats().followingCount(),
              profile.isFollowing()
          );
      }
  }
  ```

- [ ] Confirm all `@NotBlank`, `@Email`, `@Size` annotations come from `jakarta.validation.constraints.*`
