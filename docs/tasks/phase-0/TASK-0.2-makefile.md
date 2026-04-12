# TASK-0.2 — Makefile Targets

## Overview

Create a `Makefile` at the project root to provide short, memorable commands for common development workflows. This reduces onboarding friction and ensures every developer runs the exact same commands regardless of IDE or shell.

## Requirements

- `make dev` — start all backing services via Docker Compose, then launch backend and frontend dev servers concurrently.
- `make test` — run backend unit/integration tests (`mvn test`) and frontend tests (`npm run test`) sequentially.
- `make migrate` — apply pending Flyway migrations against the local database.
- `make clean` — stop all Docker Compose services and remove their volumes (fresh slate).

## Notes

- Use `$(MAKE)` rather than `make` for recursive calls to respect the current Make binary.
- Running backend and frontend concurrently in `make dev` can be done with `&` + `wait`, or by using the `concurrently` npm package if already installed.
- Guard `make migrate` with a comment explaining it runs against the `local` Spring profile.
- The Makefile is for developer convenience only — CI uses explicit commands in the workflow YAML (TASK-0.5), not `make`.

## Checklist

- [ ] Create `Makefile` at project root
- [ ] Add `dev` target:
  ```makefile
  dev:
      docker compose up -d
      cd backend && mvn spring-boot:run &
      cd frontend && npm run dev
  ```
- [ ] Add `test` target:
  ```makefile
  test:
      cd backend && mvn test
      cd frontend && npm run test
  ```
- [ ] Add `migrate` target:
  ```makefile
  migrate:
      cd backend && mvn flyway:migrate -Dspring.profiles.active=local
  ```
- [ ] Add `clean` target:
  ```makefile
  clean:
      docker compose down -v
  ```
- [ ] Add `.PHONY` declaration for all targets to prevent conflicts with files of the same name
- [ ] Verify each target runs without error on a clean checkout
