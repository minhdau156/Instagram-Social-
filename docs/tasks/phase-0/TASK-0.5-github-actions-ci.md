# TASK-0.5 — GitHub Actions CI Pipeline

## Overview

Create a GitHub Actions workflow that automatically builds, tests, and validates both the backend and frontend on every push and pull request. This gives the team immediate feedback on broken builds and failing tests before code is merged.

## Requirements

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
- The backend tests require a PostgreSQL service — use the `services` block with `postgres:15` and wait for it to be healthy, **or** use Testcontainers (which spins its own Docker container). If Testcontainers is used in TASK-0.3, no `services` block is needed.
- Do not run Playwright E2E tests in this pipeline (those are Phase 10, TASK-10.15).

## Checklist

- [ ] Create `.github/workflows/ci.yml`
- [ ] Add workflow triggers:
  ```yaml
  on:
    push:
      branches: [main]
    pull_request:
      branches: [main]
  ```
- [ ] Add `backend-ci` job:
  - [ ] Checkout code (`actions/checkout@v4`)
  - [ ] Set up Java 21 with Temurin distribution
  - [ ] Cache `~/.m2/repository` keyed on `hashFiles('**/pom.xml')`
  - [ ] Run `mvn verify` from the `backend/` directory
  - [ ] (If not using Testcontainers) Add `services.postgres` with `image: postgres:15` and health check
- [ ] Add `frontend-ci` job:
  - [ ] Checkout code
  - [ ] Set up Node 20 with `cache: npm` and `cache-dependency-path: frontend/package-lock.json`
  - [ ] Run `npm ci` from `frontend/`
  - [ ] Run `npm run build` to catch TypeScript compile errors
  - [ ] Run `npm run test` (Vitest, added in Phase 10 — skip if not yet configured; add `|| true` placeholder)
- [ ] Add build status badge to `README.md`:
  ```markdown
  ![CI](https://github.com/<owner>/<repo>/actions/workflows/ci.yml/badge.svg)
  ```
- [ ] Push to a feature branch and verify the workflow appears in the Actions tab and all jobs pass
