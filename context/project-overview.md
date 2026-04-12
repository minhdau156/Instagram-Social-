# Project Overview

## What We're Building

A social media platform inspired by Instagram — users can share photos/videos, follow each other, interact via likes/comments, and chat in real-time. See the full product spec in [`docs/prd/Business Requirements Document (BRD) for Social Media Platform.md`](../docs/prd/Business%20Requirements%20Document%20%28BRD%29%20for%20Social%20Media%20Platform.md) and the phased development roadmap in [`docs/plan.md`](../docs/plan.md).

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Backend Language | Java | 21 |
| Backend Framework | Spring Boot | 3.3.4 |
| ORM | Spring Data JPA / Hibernate | — |
| Database | PostgreSQL | 15+ |
| DB Migration | Flyway | (bundled with Spring Boot) |
| Boilerplate Reducer | Lombok | — |
| API Docs | SpringDoc OpenAPI (Swagger UI) | 2.6.0 |
| Auth | Spring Security + JWT + OAuth2 | — |
| Real-time | Spring WebSocket (STOMP) | — |
| File Storage | MinIO (local) / AWS S3 (prod) | — |
| Frontend Framework | React + TypeScript | 18 / 5 |
| Build Tool | Vite | — |
| UI Library | Material UI (MUI) | v5 |
| Data Fetching | TanStack React Query | v5 |
| HTTP Client | Axios | — |
| Real-time (FE) | SockJS + STOMP.js | — |

---

## Architecture: Hexagonal (Ports & Adapters)

The backend enforces a strict layering rule — **dependencies only point inward** toward the domain.

```
┌──────────────────────────────────────────────────────┐
│                    Infrastructure                     │
│  (Spring config, Security, Flyway, OpenAPI, Storage)  │
│  ┌────────────────────────────────────────────────┐   │
│  │              Adapters                          │   │
│  │  ┌──────────────┐   ┌───────────────────────┐ │   │
│  │  │   in/web      │   │  out/persistence      │ │   │
│  │  │ (Controllers) │   │  (JPA Adapters)       │ │   │
│  │  └──────┬───────┘   └───────────┬───────────┘ │   │
│  └─────────┼───────────────────────┼─────────────┘   │
│            │ uses Port.in           │ implements Port.out
│  ┌─────────▼───────────────────────▼─────────────┐   │
│  │               Application                      │   │
│  │          (Use-Case Services)                   │   │
│  │  ┌─────────────────────────────────────────┐   │   │
│  │  │              Domain                     │   │   │
│  │  │  model/ · port/in · port/out · exception│   │   │
│  │  └─────────────────────────────────────────┘   │   │
│  └────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────┘
```

### Backend Package Structure

```
com.instagram/
├── SocialMediaApplication.java
├── domain/
│   ├── model/          # Pure Java entities & value objects (NO framework deps)
│   ├── port/
│   │   ├── in/         # Use-case interfaces (driving ports)
│   │   └── out/        # Repository / service interfaces (driven ports)
│   ├── service/        # Domain services implementing in-ports
│   └── exception/      # Domain-specific exceptions
├── application/
│   └── usecase/        # Orchestration use-case implementations
├── adapter/
│   ├── in/
│   │   └── web/        # REST controllers + DTOs
│   └── out/
│       ├── persistence/ # JPA entities, repositories, adapters
│       └── messaging/   # WebSocket / event adapters
└── infrastructure/
    ├── config/          # Spring beans, CORS, OpenAPI
    ├── security/        # JWT filter, OAuth2 handlers
    └── storage/         # S3 / MinIO file storage
```

### Frontend Package Structure

```
frontend/src/
├── api/          # One file per domain (postsApi.ts, usersApi.ts, …)
├── components/   # Shared reusable UI components
├── pages/        # Route-level page components
├── hooks/        # Custom React hooks (useAuth, useWebSocket, …)
├── types/        # TypeScript interfaces / enums
├── App.tsx       # Root: QueryClient + ThemeProvider + Router
├── AppShell.tsx  # Layout wrapper (nav, sidebar)
└── theme.ts      # MUI theme tokens
```

---

## Database

- **Engine:** PostgreSQL 15  
- **Schema:** Managed by Flyway migrations under `backend/src/main/resources/db/migration/`  
- **Reference schema:** [`docs/database/schema.sql`](../docs/database/schema.sql) — full DDL including all ENUMs, tables, indexes, and triggers  
- **Key design choices:**  
  - All primary keys are `UUID` (generated via `uuid_generate_v4()`)  
  - Soft deletes via `deleted_at TIMESTAMPTZ` on `posts` and `comments`  
  - Denormalized counters (`like_count`, `comment_count`, `save_count`, `share_count`) kept on `posts` for read performance  
  - `pg_trgm` extension for full-text search on usernames and hashtags  
  - `citext` extension for case-insensitive username/email storage

---

## Development Phases

The project is developed in 10 phased increments (see `docs/plan.md` for details):

| Phase | Module |
|---|---|
| 0 | Foundation & Infrastructure |
| 1 | Auth & User Management |
| 2 | Posts & Media |
| 3 | Social Graph (Follow/Unfollow) |
| 4 | Interactions (Likes, Comments, Saves, Shares) |
| 5 | Feed & Discovery |
| 6 | Direct Messaging (WebSocket) |
| 7 | Notifications |
| 8 | Search |
| 9 | Content Moderation & Admin |
| 10 | Performance, Security & Polish |

---

## Local Development

| Service | URL |
|---|---|
| Backend API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| Frontend | `http://localhost:5173` |
| PostgreSQL | `localhost:5432` db=`instagram` |
| MinIO Console | `http://localhost:9001` |

Start all services:
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```
