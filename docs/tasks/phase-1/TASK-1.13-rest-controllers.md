# TASK-1.13 — REST Controllers

## Overview

Implement `AuthController` and `UserController` — the HTTP entry points for all auth and user-management operations. Controllers are thin adapters: they validate input, delegate to use-case interfaces, and return structured `ResponseEntity` responses.

## Requirements

- No business logic in controllers — delegate entirely to in-port use-case interfaces.
- Use `@Valid` on all request bodies.
- Return explicit HTTP status codes via `ResponseEntity<T>`.
- Extract current user's ID from `SecurityContextHolder` using a helper method.

## File Locations

```
backend/src/main/java/com/instagram/adapter/in/web/
├── AuthController.java
└── UserController.java
```

## Notes

- `AuthController` does NOT require authentication (all endpoints are public per the security config).
- `UserController` endpoints that act on the current user (`/me`) require authentication. `GET /users/{username}` is public.
- Use `ApiResponse<T>` wrapper from TASK-0.7 for all response bodies.
- Extract `userId` from the `Authentication` principal in `SecurityContextHolder` — create a private helper `currentUserId()` that casts the principal to `UUID`.

## Checklist

### `AuthController.java`
- [x] Annotate with `@RestController`, `@RequestMapping("/api/v1/auth")`, `@RequiredArgsConstructor`
- [x] Inject (via constructor): `RegisterUserUseCase`, `LoginUseCase`, `RefreshTokenUseCase`, `LogoutUseCase`, `RequestPasswordResetUseCase`, `ConfirmPasswordResetUseCase`
- [x] Implement `POST /register`:
  ```java
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest req) {
      User user = registerUserUseCase.register(new RegisterUserUseCase.Command(
          req.username(), req.email(), req.password(), req.fullName()));
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(UserResponse.from(user)));
  }
  ```
- [x] Implement `POST /login`:
  - Return `ApiResponse<AuthResponse>` with `200 OK`
  - Delegate to `loginUseCase.login(...)`
- [x] Implement `POST /refresh`:
  - Accept `AuthResponse` body with `refreshToken` field
  - Return new `AuthResponse` with `200 OK`
- [x] Implement `POST /logout`:
  - Accept `refreshToken` in request body
  - Return `204 No Content`
- [x] Implement `POST /password-reset/request`:
  - Accept `email` only
  - Always return `200 OK` (even if email not found — prevent user enumeration)
- [x] Implement `POST /password-reset/confirm`:
  - Accept `token` + `newPassword`
  - Return `200 OK` on success

### `UserController.java`
- [x] Annotate with `@RestController`, `@RequestMapping("/api/v1/users")`, `@RequiredArgsConstructor`
- [x] Inject: `GetUserProfileUseCase`, `UpdateProfileUseCase`
- [x] Add private helper:
  ```java
  private UUID currentUserId() {
      return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
  ```
- [x] Implement `GET /me`:
  - Calls `getUserProfileUseCase.getProfile(new Query(null, currentUserId()))` — or use a dedicated `GetCurrentUserUseCase` if preferred
  - Return `ApiResponse<UserResponse>` with `200 OK`
- [x] Implement `PUT /me`:
  - Accepts `UpdateProfileRequest`
  - Delegates to `updateProfileUseCase.updateProfile(...)`
  - Return `ApiResponse<UserResponse>` with `200 OK`
- [x] Implement `GET /{username}`:
  - Public endpoint — `currentUserId()` returns null if unauthenticated
  - Delegates to `getUserProfileUseCase.getProfile(new Query(username, currentUserId()))`
  - Return `ApiResponse<UserProfileResponse>` with `200 OK`
