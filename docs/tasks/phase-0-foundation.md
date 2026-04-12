# Phase 0 — Foundation & Infrastructure

> **Depends on:** nothing  
> **Blocks:** all subsequent phases  
> **Branch prefix:** `chore/phase-0-`

---

## Infrastructure

### TASK-0.1 — Docker Compose environment
- [ ] Create `docker-compose.yml` at project root
  - Service: `postgres` (image `postgres:15`, port `5432`, volume for data persistence)
  - Service: `minio` (image `minio/minio`, port `9000`/`9001`, env `MINIO_ROOT_USER` / `MINIO_ROOT_PASSWORD`)
  - Service: `redis` (image `redis:7-alpine`, port `6379`)
- [ ] Add `.env.example` with all required environment variables
- [ ] Verify all services start with `docker compose up -d` and pass health checks

### TASK-0.2 — Makefile targets
- [ ] Create `Makefile` in project root
  - `make dev` → starts Docker Compose + backend + frontend concurrently
  - `make test` → runs `mvn test` + `npm run test`
  - `make migrate` → runs `mvn flyway:migrate`
  - `make clean` → stops Docker Compose and removes volumes

### TASK-0.3 — Spring Boot application profiles
- [ ] Create `backend/src/main/resources/application.yml` (common config)
- [ ] Create `application-local.yml` (local PostgreSQL URL, MinIO, Redis)
- [ ] Create `application-test.yml` (H2 or Testcontainers PostgreSQL)
- [ ] Create `application-prod.yml` template (env-var placeholders only)

### TASK-0.4 — Flyway initial migration
- [ ] Copy `docs/database/schema.sql` → `backend/src/main/resources/db/migration/V1__initial_schema.sql`
- [ ] Add Flyway dependency to `pom.xml` (if not present)
- [ ] Verify migration runs cleanly on `local` profile: `mvn flyway:migrate`

### TASK-0.5 — GitHub Actions CI pipeline
- [ ] Create `.github/workflows/ci.yml`
  - Job `backend-ci`: checkout → Java 21 setup → `mvn verify`
  - Job `frontend-ci`: checkout → Node 20 setup → `npm ci` → `npm run build` → `npm run test`
  - Trigger: push to `main` + all pull requests
- [ ] Add build status badge to `README.md`

---

## Backend Foundation

### TASK-0.6 — Global exception handler
- [ ] Create `backend/.../adapter/in/web/GlobalExceptionHandler.java`
  - Annotate with `@RestControllerAdvice`
  - Handle `EntityNotFoundException` → `404`
  - Handle `ConstraintViolationException` / `MethodArgumentNotValidException` → `400`
  - Handle `AccessDeniedException` → `403`
  - Handle generic `Exception` → `500`
  - Return `ApiResponse<Void>` with error message and timestamp

### TASK-0.7 — ApiResponse wrapper
- [ ] Create `backend/.../adapter/in/web/dto/ApiResponse.java` as a Java record
  ```java
  record ApiResponse<T>(T data, String error, Instant timestamp) {
      static <T> ApiResponse<T> ok(T data) { ... }
      static ApiResponse<Void> error(String message) { ... }
  }
  ```
- [ ] Update `GlobalExceptionHandler` to use `ApiResponse`

### TASK-0.8 — BaseEntity audit fields
- [ ] Create `backend/.../adapter/out/persistence/BaseJpaEntity.java`
  - Fields: `createdAt` (`OffsetDateTime`), `updatedAt` (`OffsetDateTime`)
  - Use `@MappedSuperclass` + `@CreatedDate` / `@LastModifiedDate`
  - Enable JPA auditing with `@EnableJpaAuditing` in config

### TASK-0.9 — CORS configuration
- [ ] Create or update `backend/.../infrastructure/config/CorsConfig.java`
  - Allow origin `http://localhost:5173` for `local` profile
  - Allow methods: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`
  - Allow `Authorization` header

---

## Frontend Foundation

### TASK-0.10 — Axios instance with interceptors
- [ ] Create `frontend/src/api/client.ts`
  - Base URL from `import.meta.env.VITE_API_URL`
  - Request interceptor: attach `Authorization: Bearer <token>` from localStorage
  - Response interceptor: on 401, attempt token refresh; on failure, redirect to `/login`

### TASK-0.11 — React Query QueryClient setup
- [ ] Add `@tanstack/react-query` dependency (`npm i @tanstack/react-query`)
- [ ] Create `frontend/src/lib/queryClient.ts` with default options (staleTime, retry)
- [ ] Wrap `<App />` with `<QueryClientProvider>` in `main.tsx`
- [ ] Add `ReactQueryDevtools` for local dev

### TASK-0.12 — Error boundary component
- [ ] Create `frontend/src/components/common/ErrorBoundary.tsx`
  - Catches render errors using React error boundary pattern
  - Displays user-friendly fallback UI with a "Try again" button

### TASK-0.13 — Loading & Skeleton components
- [ ] Create `frontend/src/components/common/PageLoader.tsx` — full-page centered spinner
- [ ] Create `frontend/src/components/common/SkeletonCard.tsx` — MUI Skeleton for post card shape
- [ ] Create `frontend/src/components/common/SkeletonList.tsx` — repeats `SkeletonCard` n times
