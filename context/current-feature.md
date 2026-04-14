# Current Feature: TASK-0.5 — GitHub Actions CI Pipeline

## Status

In Progress

## Goals

- Two separate jobs: `backend-ci` and `frontend-ci`, running in parallel.
- Backend job: compile with Maven and run all tests (`mvn verify`).
- Frontend job: install dependencies, build the app, and run unit tests.
- Trigger on: push to `main` branch and all pull requests targeting `main`.
- A build status badge must be added to the project `README.md`.

## Notes

- Use `actions/setup-java@v4` with `distribution: temurin` and `java-version: 21` for the backend job.
- Use `actions/setup-node@v4` with `node-version: 20` for the frontend job.
- Cache Maven dependencies with `actions/cache` keyed on `pom.xml` hash to speed up subsequent runs.
- Cache npm dependencies with `actions/setup-node`'s built-in `cache: npm` option.
- The backend tests require a PostgreSQL service — use the `services` block with `postgres:15` and wait for it to be healthy, **or** use Testcontainers.
- Do not run Playwright E2E tests in this pipeline (those are Phase 10, TASK-10.15).

## History

- TASK-0.1 — Initialize Project Setup and Configuration
- TASK-0.2 — Makefile Automation Hub
- TASK-0.3 — Docker Local Infrastructure
- TASK-0.4 — Flyway Initial Migration
