# TASK-0.1 — Docker Compose Environment

## Overview

Set up the local development infrastructure using Docker Compose. This task provisions all backing services (PostgreSQL, MinIO, Redis) needed by the backend, so every developer gets an identical, reproducible local environment with a single command.

## Requirements

- **PostgreSQL 15** — primary data store for the application.
- **MinIO** — S3-compatible object storage for media uploads (replaces AWS S3 in local dev).
- **Redis 7** — used later for feed caching (Phase 5/10); provision now to avoid reconfiguring compose later.
- An `.env.example` file documenting all required environment variables so new contributors know what to set.
- All services must expose health checks so dependent services (and CI) can wait for readiness.

## Notes

- Use named Docker volumes (not bind mounts) for PostgreSQL data so data survives container restarts.
- MinIO requires two ports: `9000` (S3 API) and `9001` (web console). Expose both.
- Prefix all env vars with the service name (e.g., `POSTGRES_DB`, `MINIO_ROOT_USER`) to avoid collision.
- Do **not** commit actual secrets — `.env` is gitignored; only `.env.example` is committed.
- Redis does not need persistence in local dev — `redis:7-alpine` with no volume is fine.

## Checklist

- [x] Create `docker-compose.yml` at project root
  - [x] Service `postgres`: image `postgres:15`, port `5432`, named volume `postgres_data`, env vars `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, healthcheck using `pg_isready`
  - [x] Service `minio`: image `minio/minio`, ports `9000` and `9001`, named volume `minio_data`, env vars `MINIO_ROOT_USER` / `MINIO_ROOT_PASSWORD`, command `server /data --console-address ":9001"`, healthcheck via `curl -f http://localhost:9000/minio/health/live`
  - [x] Service `redis`: image `redis:7-alpine`, port `6379`, healthcheck via `redis-cli ping`
- [x] Create `.env.example` at project root with all required variables:
  ```
  POSTGRES_DB=instagram
  POSTGRES_USER=instagram
  POSTGRES_PASSWORD=changeme
  MINIO_ROOT_USER=minioadmin
  MINIO_ROOT_PASSWORD=minioadmin
  REDIS_URL=redis://localhost:6379
  ```
- [x] Verify all services start cleanly: `docker compose up -d`
- [x] Verify all health checks pass: `docker compose ps` shows `(healthy)` for all services
