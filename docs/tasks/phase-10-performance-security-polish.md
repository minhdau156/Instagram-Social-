# Phase 10 — Performance, Security & Polish

> **Depends on:** All previous phases  
> **BRD refs:** NFR-001 → NFR-013  
> **Branch prefix:** `chore/phase-10-` or `fix/phase-10-`

---

## Performance

### TASK-10.1 — Redis caching for feed & profiles
- [ ] Add `spring-boot-starter-data-redis` to `pom.xml`
- [ ] Create `RedisConfig.java` in `infrastructure/config/` — configure `RedisTemplate<String, Object>` with JSON serializer
- [ ] Wrap `FeedService.getHomeFeed(cursor=null)` with a 60-second Redis cache (key: `feed:{userId}:page1`)
- [ ] Wrap `UserService.getUserProfile(username)` with a 5-minute Redis cache (key: `profile:{username}`)
- [ ] Evict profile cache on `UpdateProfileService` execution
- [ ] Add `Cache-Control: public, max-age=300` header on `GET /api/v1/users/{username}` for public profiles

### TASK-10.2 — N+1 query review
- [ ] Audit all `@OneToMany` and `@ManyToOne` relationships — replace `FetchType.EAGER` with `FetchType.LAZY` where missing
- [ ] Add `@EntityGraph` or `JOIN FETCH` to queries that need multiple associations in one call (e.g., `PostJpaRepository` loading `PostMedia` in the feed)
- [ ] Run integration tests with Hibernate statistics enabled to verify no N+1 on feed endpoint

### TASK-10.3 — CDN-backed media URLs
- [ ] Update `MinioStorageAdapter.generatePresignedPutUrl` to produce URLs rooted at `VITE_CDN_BASE_URL` env var
- [ ] Add CloudFront / MinIO CDN proxy config example to `docs/` (optional: `docs/infra/cdn-setup.md`)

### TASK-10.4 — Frontend image optimization
- [ ] Add `loading="lazy"` to all `<img>` tags in `PostCard`, `PostGrid`, `ProfilePage`
- [ ] Serve AVIF/WebP from backend (hint via `Accept` header handling in `MediaController`)
- [ ] Run `vite-bundle-visualizer` to identify oversized chunks; apply dynamic import (`React.lazy`) to large page components

---

## Security

### TASK-10.5 — JWT hardening
- [ ] Verify access token expiry = 15 min, refresh token expiry = 7 days in `JwtTokenProvider`
- [ ] Use RS256 (asymmetric) key pair instead of HS256 if not already done; store private key in env var
- [ ] Implement refresh token rotation — invalidate old token on each `/auth/refresh` call

### TASK-10.6 — Rate limiting
- [ ] Add `bucket4j-spring-boot-starter` dependency to `pom.xml`
- [ ] Configure rate limits per IP:
  - `/api/v1/auth/register` → 5 req / 10 min
  - `/api/v1/auth/login` → 10 req / 1 min
  - All other endpoints → 200 req / 1 min
- [ ] Return `429 Too Many Requests` with `Retry-After` header on limit exceeded

### TASK-10.7 — Input validation hardening
- [ ] Audit all request DTOs — ensure every field has `@NotNull`/`@NotBlank`/`@Size`/`@Pattern` where appropriate
- [ ] Add `@ControllerAdvice` handler for `HttpMessageNotReadableException` (malformed JSON) → `400`
- [ ] Strip HTML from all user-generated text fields using OWASP AntiSamy or plain regex in a `@BeforeMapping` hook

### TASK-10.8 — OWASP dependency check in CI
- [ ] Add `org.owasp:dependency-check-maven` plugin to `pom.xml`
- [ ] Add `mvn dependency-check:check` step to `.github/workflows/ci.yml` (fail on CVSS ≥ 7)

### TASK-10.9 — HTTPS / TLS
- [ ] Document TLS termination in `docs/infra/tls-setup.md` (nginx / AWS ALB config)
- [ ] Add `server.forward-headers-strategy=FRAMEWORK` in `application-prod.yml` for `X-Forwarded-Proto` handling
- [ ] Set `Strict-Transport-Security` header in `SecurityConfig`

---

## Observability

### TASK-10.10 — Structured logging & MDC
- [ ] Follow the `logging-patterns` skill instructions
- [ ] Create `MdcLoggingFilter.java` — sets `requestId`, `userId`, `method`, `path` in MDC for every request
- [ ] Update `logback-spring.xml` to output JSON format in non-local profiles
- [ ] Replace any remaining `System.out.println` with SLF4J calls

### TASK-10.11 — Actuator & Micrometer
- [ ] Add `spring-boot-starter-actuator` + `micrometer-registry-prometheus` to `pom.xml`
- [ ] Expose `health`, `info`, `metrics`, `prometheus` endpoints
- [ ] Add custom `MeterRegistry` counter for post creations, likes, registrations
- [ ] Secure Actuator endpoints (allow only `ROLE_ADMIN` except `/actuator/health`)

### TASK-10.12 — Distributed tracing
- [ ] Add `io.micrometer:micrometer-tracing-bridge-otel` + `opentelemetry-exporter-zipkin` to `pom.xml`
- [ ] Configure `application.yml` → `management.tracing.sampling.probability=1.0` for dev
- [ ] Run Zipkin locally via Docker Compose (`openzipkin/zipkin` image)

---

## Testing

### TASK-10.13 — Backend test coverage gate
- [ ] Add `jacoco-maven-plugin` to `pom.xml`, fail build if coverage < 80%
- [ ] Identify and fill test gaps from phases 1–9 (prioritize domain services and controllers)
- [ ] Ensure all exception handler branches are covered

### TASK-10.14 — Frontend component tests
- [ ] Add `vitest` + `@testing-library/react` + `msw` (mock service worker) to `package.json`
- [ ] Write tests for:
  - `LoginPage` — form validation, submit calls API, error state
  - `PostCard` — renders media, like/save toggles, comment count
  - `LikeButton` — optimistic update
  - `ProtectedRoute` — redirects unauthenticated users
  - `useWebSocket` hook — subscription + message handling

### TASK-10.15 — E2E smoke tests (Playwright)
- [ ] Add `@playwright/test` to `package.json`
- [ ] Create `e2e/` directory with tests:
  - `auth.spec.ts` — register → login → view profile
  - `posts.spec.ts` — create post → view in feed → like → comment
  - `follow.spec.ts` — follow user → see posts in feed → unfollow
  - `messaging.spec.ts` — open DM → send message → verify delivery
- [ ] Add Playwright step to CI (run against the Docker Compose stack)

---

## DevOps

### TASK-10.16 — Dockerfile: backend
- [ ] Create `backend/Dockerfile` (multi-stage):
  - Stage 1 (`build`): `maven:3.9-eclipse-temurin-21` — `mvn package -DskipTests`
  - Stage 2 (`run`): `eclipse-temurin:21-jre-alpine` — copy JAR, `EXPOSE 8080`, `ENTRYPOINT`

### TASK-10.17 — Dockerfile: frontend
- [ ] Create `frontend/Dockerfile` (multi-stage):
  - Stage 1 (`build`): `node:20-alpine` — `npm ci && npm run build`
  - Stage 2 (`serve`): `nginx:alpine` — copy `dist/`, configure SPA fallback in `nginx.conf`

### TASK-10.18 — Full docker-compose.yml
- [ ] Update `docker-compose.yml` to include all services:
  - `backend` (depends on `postgres`, `redis`)
  - `frontend` (depends on `backend`)
  - `postgres`, `minio`, `redis`, `zipkin`
- [ ] Add `healthcheck` blocks for all services
- [ ] Document startup order and port mapping in `README.md`

### TASK-10.19 — CI/CD: Docker build & push
- [ ] Extend `.github/workflows/ci.yml`:
  - After tests pass: `docker build` backend + frontend images
  - Push to GitHub Container Registry (`ghcr.io`)
  - Tag with `sha:${{ github.sha }}` and `latest` on main branch pushes
