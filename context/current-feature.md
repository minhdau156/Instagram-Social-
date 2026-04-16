# Current Feature: TASK-1.5 — In-Ports (Use-Case Interfaces)

## Status

Not Started

## Goals

- Define `RegisterUserUseCase` — registers a new user, returns `User`
- Define `LoginUseCase` — authenticates via identifier+password, returns `AuthResult`
- Define `RefreshTokenUseCase` — exchanges refresh token for new `AuthResult`
- Define `LogoutUseCase` — invalidates a refresh token (void)
- Define `RequestPasswordResetUseCase` — triggers password reset email (void)
- Define `ConfirmPasswordResetUseCase` — applies new password via reset token (void)
- Define `GetUserProfileUseCase` — returns `UserProfile` (User + UserStats + isFollowing)
- Define `UpdateProfileUseCase` — updates profile fields, returns updated `User`
- Create `AuthResult` value-object record (`accessToken`, `refreshToken`, `expiresIn`)
- Create `UserProfile` value-object record (`user`, `stats`, `isFollowing`)
- All interfaces are pure Java — zero Spring or JPA annotations

## Notes

- All files live in `domain/port/in/` — one file per use case.
- Each interface has **exactly one** method; input is always an inner `record` named `Command` (writes) or `Query` (reads).
- `LoginUseCase.Command.identifier` accepts either username or email; the service resolves which.
- `AuthResult` and `UserProfile` are value objects — place them in `domain/model/`.
- `deleteById` is intentionally absent; soft-delete via `withDeactivated()` on `UserService`.
- No framework dependencies anywhere in this package.

## History

- TASK-0.1 — Initialize Project Setup and Configuration
- TASK-0.2 — Makefile Automation Hub
- TASK-0.3 — Docker Local Infrastructure
- TASK-0.4 — Flyway Initial Migration
- TASK-0.5 — GitHub Actions CI Pipeline
- TASK-0.6 — Global Exception Handler
- TASK-0.7 — API Response Wrapper
- TASK-0.8 — BaseEntity Audit Fields
- TASK-0.9 — CORS Configuration
- TASK-0.10 — Axios Instance with Interceptors
- TASK-0.11 — React Query QueryClient Setup
- TASK-0.12 — Error Boundary Component
- TASK-0.13 — Loading & Skeleton Components
- TASK-1.1 — Domain Model: User
- TASK-1.2 — Domain Model: UserStats
- TASK-1.3 — Domain Exceptions
- TASK-1.4 — Out-Port: UserRepository
