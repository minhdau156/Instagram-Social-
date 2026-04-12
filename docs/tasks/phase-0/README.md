# Phase 0 — Foundation & Infrastructure

> **Depends on:** nothing  
> **Blocks:** all subsequent phases  
> **Branch prefix:** `chore/phase-0-`  
> **Status:** 🟡 Partially Done

---

## Task List

### Infrastructure

| Task | File | Description | Status |
|---|---|---|---|
| TASK-0.1 | [TASK-0.1-docker-compose.md](./TASK-0.1-docker-compose.md) | Docker Compose — PostgreSQL, MinIO, Redis | ⬜ |
| TASK-0.2 | [TASK-0.2-makefile.md](./TASK-0.2-makefile.md) | Makefile — dev, test, migrate, clean targets | ⬜ |
| TASK-0.3 | [TASK-0.3-spring-profiles.md](./TASK-0.3-spring-profiles.md) | Spring Boot application profiles (local/test/prod) | ⬜ |
| TASK-0.4 | [TASK-0.4-flyway-migration.md](./TASK-0.4-flyway-migration.md) | Flyway — V1 initial schema migration | ⬜ |
| TASK-0.5 | [TASK-0.5-github-actions-ci.md](./TASK-0.5-github-actions-ci.md) | GitHub Actions CI pipeline | ⬜ |

### Backend Foundation

| Task | File | Description | Status |
|---|---|---|---|
| TASK-0.6 | [TASK-0.6-global-exception-handler.md](./TASK-0.6-global-exception-handler.md) | Global exception handler (`@RestControllerAdvice`) | ⬜ |
| TASK-0.7 | [TASK-0.7-api-response-wrapper.md](./TASK-0.7-api-response-wrapper.md) | `ApiResponse<T>` envelope record | ⬜ |
| TASK-0.8 | [TASK-0.8-base-jpa-entity.md](./TASK-0.8-base-jpa-entity.md) | `BaseJpaEntity` with JPA audit fields | ⬜ |
| TASK-0.9 | [TASK-0.9-cors-config.md](./TASK-0.9-cors-config.md) | CORS configuration | ⬜ |

### Frontend Foundation

| Task | File | Description | Status |
|---|---|---|---|
| TASK-0.10 | [TASK-0.10-axios-instance.md](./TASK-0.10-axios-instance.md) | Axios instance with JWT interceptors and 401 refresh | ⬜ |
| TASK-0.11 | [TASK-0.11-react-query-setup.md](./TASK-0.11-react-query-setup.md) | React Query `QueryClient` setup | ⬜ |
| TASK-0.12 | [TASK-0.12-error-boundary.md](./TASK-0.12-error-boundary.md) | React error boundary component | ⬜ |
| TASK-0.13 | [TASK-0.13-loading-skeleton-components.md](./TASK-0.13-loading-skeleton-components.md) | `PageLoader`, `SkeletonCard`, `SkeletonList` | ⬜ |

---

## Suggested Implementation Order

```
TASK-0.1 (Docker)
    ↓
TASK-0.3 (Spring Profiles)
    ↓
TASK-0.4 (Flyway)  ←→  TASK-0.7 (ApiResponse)
    ↓                        ↓
TASK-0.8 (BaseJpaEntity)   TASK-0.6 (ExceptionHandler)
    ↓                        ↓
TASK-0.9 (CORS)  ←──────────┘
    ↓
TASK-0.2 (Makefile)  +  TASK-0.5 (CI)   [can be done in parallel]
    ↓
TASK-0.10 (Axios)
    ↓
TASK-0.11 (React Query)
    ↓
TASK-0.12 (Error Boundary)  +  TASK-0.13 (Skeletons)  [parallel]
```
