# Current Feature: TASK-4.4 — In-Ports: Like Use Cases

## Status
Not Started

## Goals
- Create `LikePostUseCase` interface with nested `Command`
- Create `UnlikePostUseCase` interface with nested `Command`
- Create `LikeCommentUseCase` interface with nested `Command`
- Create `UnlikeCommentUseCase` interface with nested `Command`
- Create `GetPostLikersUseCase` interface with nested `Query` returning `Page<UserSummary>`

## Notes
- Lives in `domain/port/in/` — no Spring or JPA annotations.
- Each interface declares exactly one method.
- Commands/Queries are record types nested inside the interface.
- Follow the naming and structure of existing use cases.
- `GetPostLikersUseCase` returns a domain `Page<UserSummary>`.
- `requestingUserId` may be `null` for unauthenticated callers.
- Verify that all `Command` / `Query` field types are `UUID`, `int`, or domain types only.

## History

- TASK-4.3 — Out-Port: LikeRepository
- TASK-4.2 — Domain Exceptions: AlreadyLikedException & NotLikedException + GlobalExceptionHandler mappings
- TASK-4.1 — Domain Models: PostLike & CommentLike
- TASK-3.19 — Integrate FollowButton into ProfilePage
- TASK-3.18 — Follow Requests Page

- TASK-3.15 — API Services
- TASK-3.14 — TypeScript Types
- TASK-3.13 — Unit & Integration Tests
- TASK-3.10 — REST Controller: FollowController
- TASK-3.9 — user_stats JPA Entity & Repository
- TASK-3.8 — Persistence Adapter: FollowPersistenceAdapter
- TASK-3.6 — JPA Entity: FollowJpaEntity

- TASK-1.22 — Auth & Profile Pages
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
- TASK-1.5 — In-Ports: Use-Case Interfaces
- TASK-1.6 — Domain Service: UserService
- TASK-1.7 — JPA Entity: UserJpaEntity
- TASK-1.8 — JPA Repository: UserJpaRepository
- TASK-1.9 — Persistence Adapter: UserPersistenceAdapter
- TASK-1.10 — Security Infrastructure (JWT)
- TASK-1.11 — OAuth2 (Google / Facebook)
- TASK-1.12 — Password Hash & Email Adapters (Out-Ports)
- TASK-1.13 - REST Controllers: UserController & AuthController
- TASK-1.14 — Request / Response DTOs
- TASK-1.15 — Avatar Upload
- TASK-1.16 — GlobalExceptionHandler: Auth Exception Mappings
- Integrate Swagger to Auth Feature
- TASK-1.17 - Test: Unit Test for UserService and Integration Test for AuthController
- TASK-1.18 — TypeScript Types
- TASK-1.19 — API Services
- TASK-1.20 — AuthContext & useAuth Hook
- Convert Auth Pages to react-hook-form (Phase 1 Complete)
- TASK-2.1 — Domain model: Post aggregate
- TASK-2.2 — Domain model: Hashtag
- TASK-2.3 — Domain exceptions
- TASK-2.4 — Out-ports
- TASK-2.5 — In-ports (one file each)
- TASK-2.6 — Domain service: PostService
- TASK-2.7 — JPA entities
- TASK-2.8 — JPA repositories
- TASK-2.9 — Persistence adapters
- TASK-2.10 — MinIO pre-signed URL adapter
- TASK-2.11 — REST controllers & DTOs
- TASK-2.12 — Register exception mappings
- TASK-2.13 — Tests
- TASK-2.14 — TypeScript types
- TASK-2.15 — API services
- TASK-2.16 — Custom hooks
- TASK-2.17 — Media upload component
- TASK-2.18 — Caption editor component
- TASK-2.19 — Post creation page/modal
- TASK-2.20 — Post display components
- TASK-3.1 — Domain Model: Follow
- TASK-3.2 — Domain Exceptions
- TASK-3.4 — In-Ports (Use-Case Interfaces)
- TASK-3.5 — Domain Service: FollowService
- TASK-3.7 — JPA Repository: FollowJpaRepository
- TASK-3.12 — GlobalExceptionHandler — Follow Mappings
- TASK-3.16 - Custom Hooks
- TASK-3.17 — Follow Components
