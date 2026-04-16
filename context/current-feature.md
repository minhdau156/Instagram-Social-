# Current Feature: TASK-1.1 — Domain Model: User

## Status

Not Started

## Goals

- Create `UserStatus.java` enum (`ACTIVE`, `DEACTIVATED`, `SUSPENDED`) in `domain/model/`
- Create `User.java` pure domain entity with all required fields (no framework/Lombok annotations)
- Implement private no-arg constructor + static inner `Builder` with required-field validation on `build()`
- Implement private `copy()` helper returning a mutable Builder copy
- Implement `withUpdatedProfile(fullName, bio, avatarUrl, isPrivate)` → copy-builder idiom
- Implement `withDeactivated()` → copy with `status = DEACTIVATED`
- Implement `withAvatarUrl(String avatarUrl)` convenience method
- Implement `isActive()` predicate: `return this.status == UserStatus.ACTIVE`
- Write `UserTest.java` with 4 unit tests covering builder, null validation, deactivation, and immutability

## Notes

- Files: `backend/src/main/java/com/instagram/domain/model/User.java` and `UserStatus.java`
- **Pure domain** — no `@Entity`, `@Component`, Lombok, or any framework dependency
- Reference `Post.java` for the established hand-written Builder pattern
- `passwordHash` is the BCrypt hash — never raw password; nullable for OAuth2 users
- `isPrivate` and `isVerified` default to `false`
- Do **not** add `createdAt`/`updatedAt` — those belong to `BaseJpaEntity`
- `Builder.build()` must throw `IllegalStateException` if `id`, `username`, `email`, or `status` is null

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
