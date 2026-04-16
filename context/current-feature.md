# Current Feature: TASK-1.3 — Domain Exceptions

## Status

Not Started

## Goals

- Create `UserNotFoundException` with `withUsername(String)` and `withId(UUID)` static factories
- Create `UserAlreadyExistsException` with `(String field, String value)` constructor
- Create `InvalidCredentialsException` with a no-arg constructor and a fixed safe message
- Create `PasswordResetTokenExpiredException` with a no-arg constructor and a fixed safe message
- Verify no Spring/framework annotations appear in any exception class

## Notes

- Package: `backend/src/main/java/com/instagram/domain/exception/`
- All extend `RuntimeException` — unchecked, no framework dependencies
- Messages must be user-safe (no stack traces, no internal IDs)
- These will be mapped to HTTP status codes in TASK-1.16 (`GlobalExceptionHandler`)
- No unit tests required — exceptions are trivially simple; testing happens via service layer tests

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
