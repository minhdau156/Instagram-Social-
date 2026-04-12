# Instagram

## Context Files

Read the following to get the full context of the project:

- @context/project-overview.md — tech stack, architecture, package structure, local dev URLs
- @context/coding-standard.md — naming conventions, patterns, and rules for all generated code
- @context/ai-interaction.md — how to work with AI: feature implementation order, what to never do, response format
- @context/current-feature.md — the active feature/task being worked on right now

## Key Docs

- **Roadmap:** `docs/plan.md`
- **DB Schema (source of truth):** `docs/database/schema.sql`
- **BRD:** `docs/prd/Business Requirements Document (BRD) for Social Media Platform.md`

## Commands

**IMPORTANT:** Do not add Claude to any commit messages

```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

| Service | URL |
|---|---|
| Backend API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| Frontend | `http://localhost:5173` |

### Git

```
# Branch
feat/<desc> | fix/<desc> | refactor/<desc> | chore/<desc>

# Commits (Conventional Commits)
feat(posts): add soft-delete use case
fix(auth): handle expired refresh token 401
```