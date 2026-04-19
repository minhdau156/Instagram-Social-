# Current Feature: Integrate Swagger to Auth Feature

## Status

In Progress

## Goals

- Add `@Tag` OpenAPI annotations to `AuthController` and `UserController` for proper API grouping.
- Add `@Operation` annotations to all authentication and user management endpoints, detailing their purpose.
- Add `@ApiResponses` to document expected success and error HTTP statuses (e.g., 200, 400, 401, 404, 409).
- Decorate Request and Response DTOs with `@Schema` annotations to provide example values and descriptions.
- Ensure the Swagger documentation accurately reflects the validation constraints and exception mappings.

## Notes

- Swagger UI is already available at `http://localhost:8080/swagger-ui.html`. This task focuses on adding specific endpoint documentation.
- Do not expose any sensitive internal details or stack traces in the API definitions.

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
