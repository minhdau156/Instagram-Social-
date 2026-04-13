# TASK-0.3 — Spring Boot Application Profiles

## Overview

Configure Spring Boot's `application.yml` profile system so the backend can run with environment-appropriate settings (local dev, test, production) without changing code. This is foundational — every subsequent backend task depends on being able to connect to the database, MinIO, and Redis.

## Requirements

- A shared `application.yml` with settings common to all environments (app name, OpenAPI info, Flyway config, etc.).
- A `application-local.yml` for local development pointing at Docker Compose services.
- A `application-test.yml` for running `@SpringBootTest` / `@DataJpaTest` (isolated database).
- A `application-prod.yml` that contains only env-var placeholders — no hardcoded values.

## Notes

- Use `spring.config.activate.on-profile` to scope profile-specific properties (Spring Boot 3 style).
- For the test profile, prefer **Testcontainers** over H2 to keep tests closer to production behavior. If Testcontainers is not yet set up, H2 is acceptable as a placeholder with a `// TODO` comment.
- The `local` profile should set `spring.jpa.show-sql=true` and `spring.jpa.hibernate.ddl-auto=validate` (Flyway owns schema creation, not Hibernate).
- Never put real credentials in committed YML files — use `${ENV_VAR:default}` syntax for locals only.
- Active profile is set via `SPRING_PROFILES_ACTIVE` env var or `-Dspring.profiles.active=local` JVM arg.

## Checklist

- [x] Create `backend/src/main/resources/application.yml` with shared config:
  - `spring.application.name: instagram`
  - `spring.jpa.hibernate.ddl-auto: validate`
  - `spring.flyway.enabled: true`
  - OpenAPI / Swagger base info
- [x] Create `backend/src/main/resources/application-local.yml`:
  - `spring.datasource.url: jdbc:postgresql://localhost:5432/instagram`
  - `spring.datasource.username: ${POSTGRES_USER:instagram}`
  - `spring.datasource.password: ${POSTGRES_PASSWORD:changeme}`
  - `spring.data.redis.url: ${REDIS_URL:redis://localhost:6379}`
  - MinIO endpoint, access key, secret (from env vars with defaults)
  - `spring.jpa.show-sql: true`
  - `logging.level.com.instagram: DEBUG`
- [x] Create `backend/src/main/resources/application-test.yml`:
  - Use Testcontainers PostgreSQL auto-configuration **or** H2 in-memory with `MODE=PostgreSQL`
  - `spring.flyway.enabled: true` (run migrations against test DB)
  - `spring.jpa.show-sql: false`
- [x] Create `backend/src/main/resources/application-prod.yml`:
  - All values as `${ENV_VAR}` with no defaults (fail fast if env is missing)
  - `spring.jpa.show-sql: false`
  - `logging.level.root: WARN`
- [x] Verify the application starts on the `local` profile: `mvn spring-boot:run -Dspring.profiles.active=local`
