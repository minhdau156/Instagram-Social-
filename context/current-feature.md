# Current Feature: Post creation page/modal

## Status

Not Started

## Goals

- Implement `CreatePostModal` component using Material UI `Dialog` and `Stepper`.
- Create a multi-step form: Step 1 (`MediaPicker`), Step 2 (`MediaCropEditor`), Step 3 (`CaptionEditor` + Location text input).
- Submit form data to the backend using the `useCreatePost` hook.
- Implement responsive design and basic error handling.
- Write a unit test for this feature.

## Notes

- **File:** `frontend/src/components/posts/CreatePostModal.tsx`
- **Dependencies:** Uses `CaptionEditor`, `MediaPicker` (Step 1), and `MediaCropEditor` (Step 2) components. Ensure they are correctly imported or stubbed.
## History

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
