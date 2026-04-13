# TASK-0.4 — Flyway Initial Migration

## Overview

Integrate Flyway into the Spring Boot backend and seed the database schema from the project's source-of-truth SQL file (`docs/database/schema.sql`). After this task, any developer can spin up a fresh PostgreSQL instance and have the full schema applied automatically on application startup.

## Requirements

- Flyway must run automatically when the application starts (on `local` and `prod` profiles).
- The initial migration file must be named exactly `V1__initial_schema.sql` to follow Flyway versioning conventions.
- Schema must match `docs/database/schema.sql` exactly — do **not** alter table names, column names, or types.
- Flyway must be idempotent: re-running on an already-migrated database does nothing (this is Flyway's default behavior).

## Notes

- Flyway dependency is `org.flywaydb:flyway-core` + `org.flywaydb:flyway-database-postgresql` for Spring Boot 3.x.
- Migration files live in `backend/src/main/resources/db/migration/` — Spring Boot auto-configures this path.
- The version number `V1` is permanent. Future schema changes will be `V2`, `V3`, etc. Never modify `V1` after it has been applied to any environment.
- Set `spring.flyway.baseline-on-migrate=false` (default) to avoid accidentally baselining an existing schema.
- For the test profile: Flyway should also run so tests operate against a real schema, not Hibernate's `create-drop`.

## Checklist

- [x] Add Flyway dependencies to `backend/pom.xml` (if not already present):
  ```xml
  <dependency>
      <groupId>org.flywaydb</groupId>
      <artifactId>flyway-core</artifactId>
  </dependency>
  <dependency>
      <groupId>org.flywaydb</groupId>
      <artifactId>flyway-database-postgresql</artifactId>
  </dependency>
  ```
- [x] Create directory `backend/src/main/resources/db/migration/`
- [x] Copy `docs/database/schema.sql` → `backend/src/main/resources/db/migration/V1__initial_schema.sql`
  - Review the copy for any psql-client-only syntax (e.g., `\c`, `\i`) that must be removed
  - Ensure all `CREATE TABLE` statements include `IF NOT EXISTS` or rely on Flyway's versioned guarantee
- [x] Ensure `spring.flyway.enabled=true` in `application.yml`
- [x] Start the application on `local` profile and verify: `mvn spring-boot:run -Dspring.profiles.active=local`
- [x] Confirm migration success in logs: `Successfully applied 1 migration to schema "public"`
- [x] Run `mvn flyway:migrate` via the Makefile target and confirm it is a no-op on second run
