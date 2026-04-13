# Current Feature: TASK-0.4 — Flyway Initial Migration

## Status

In Progress

## Goals

- Integrate Flyway into the Spring Boot backend
- Seed the database schema from the project's source-of-truth SQL file (`docs/database/schema.sql`)
- Flyway must run automatically when the application starts (on `local` and `prod` profiles).
- The initial migration file must be named exactly `V1__initial_schema.sql` to follow Flyway versioning conventions.
- Schema must match `docs/database/schema.sql` exactly
- Flyway must be idempotent: re-running on an already-migrated database does nothing.

## Notes

- Flyway dependency is `org.flywaydb:flyway-core` + `org.flywaydb:flyway-database-postgresql` for Spring Boot 3.x.
- Migration files live in `backend/src/main/resources/db/migration/`
- The version number `V1` is permanent.
- Set `spring.flyway.baseline-on-migrate=false` (default)
- For the test profile: Flyway should also run so tests operate against a real schema, not Hibernate's `create-drop`.
- Complete the checklist from the spec.

## History
