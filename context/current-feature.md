# TASK-0.8 — BaseEntity Audit Fields

## Status

Not Started

## Goals

- Create a `@MappedSuperclass` JPA base entity that automatically populates `created_at` and `updated_at` timestamps on every entity.
- Timestamps are set automatically by JPA auditing — never set manually in application code.
- `createdAt` is set once on persist and must be immutable thereafter (`updatable = false`).
- `updatedAt` is refreshed on every `merge` operation.
- JPA auditing must be globally enabled.

## Notes

- Location: `adapter/out/persistence/BaseJpaEntity.java` — the base class lives in the persistence adapter layer.
- Use Spring Data JPA's auditing annotations: `@CreatedDate` and `@LastModifiedDate`. These require `@EnableJpaAuditing` on a config class.
- The field type must be `OffsetDateTime` (not `LocalDateTime`) to store timezone-aware timestamps matching the PostgreSQL `TIMESTAMPTZ` columns in the schema.
- JPA auditing config should go in an existing or new `JpaConfig.java` in `infrastructure/config/`.
- Do not use Lombok on this class — use `@Getter` from Lombok only if the project already uses it in the persistence layer. Per coding standards, Lombok is allowed in the `adapter/` layer.

## History

- TASK-0.1 — Initialize Project Setup and Configuration
- TASK-0.2 — Makefile Automation Hub
- TASK-0.3 — Docker Local Infrastructure
- TASK-0.4 — Flyway Initial Migration
- TASK-0.5 — GitHub Actions CI Pipeline
- TASK-0.6 — Global Exception Handler
- TASK-0.7 — API Response Wrapper
