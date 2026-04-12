# Task Index

> Auto-generated from `docs/plan.md` — break-down of all phases into atomic, implementable tasks.
> Each task file maps exactly to one phase and follows the Hexagonal Architecture implementation order defined in `context/ai-interaction.md`.

---

## Implementation Order

```
Phase 0 → Phase 1 → Phase 2 → Phase 3 → Phase 4 → Phase 5
                                                ↓
                         Phase 6 → Phase 7 → Phase 8 → Phase 9 → Phase 10
```

---

## Phase Files

| Phase | File | Status | Task Count |
|---|---|---|---|
| Phase 0 — Foundation & Infrastructure | [phase-0-foundation.md](./phase-0-foundation.md) | 🟡 Partially Done | 13 tasks |
| Phase 1 — Authentication & User Management | [phase-1-auth-user-management.md](./phase-1-auth-user-management.md) | ⬜ Not Started | 23 tasks |
| Phase 2 — Posts & Media | [phase-2-posts-media.md](./phase-2-posts-media.md) | 🟡 Partially Done | 22 tasks |
| Phase 3 — Social Graph (Follow/Unfollow) | [phase-3-social-graph.md](./phase-3-social-graph.md) | ⬜ Not Started | 20 tasks |
| Phase 4 — Social Interactions | [phase-4-social-interactions.md](./phase-4-social-interactions.md) | ⬜ Not Started | 35 tasks |
| Phase 5 — Feed & Discovery | [phase-5-feed-discovery.md](./phase-5-feed-discovery.md) | ⬜ Not Started | 17 tasks |
| Phase 6 — Direct Messaging | [phase-6-direct-messaging.md](./phase-6-direct-messaging.md) | ⬜ Not Started | 21 tasks |
| Phase 7 — Notifications | [phase-7-notifications.md](./phase-7-notifications.md) | ⬜ Not Started | 21 tasks |
| Phase 8 — Search | [phase-8-search.md](./phase-8-search.md) | ⬜ Not Started | 15 tasks |
| Phase 9 — Content Moderation & Admin | [phase-9-moderation-admin.md](./phase-9-moderation-admin.md) | ⬜ Not Started | 18 tasks |
| Phase 10 — Performance, Security & Polish | [phase-10-performance-security-polish.md](./phase-10-performance-security-polish.md) | ⬜ Not Started | 19 tasks |

---

## Task Naming Convention

Tasks are named `TASK-<phase>.<sequence>`. When working on a task:

1. Open the relevant phase file.
2. Check off `[ ]` → `[x]` as you complete sub-items.
3. Update `context/current-feature.md` to reflect the active task.

## Per-Task Implementation Order (Backend)

Every backend feature follows this strict order (from `context/ai-interaction.md`):

1. Domain model → `domain/model/`
2. Out-port → `domain/port/out/`
3. In-port(s) → `domain/port/in/`
4. Domain service → `domain/service/`
5. JPA entity → `adapter/out/persistence/`
6. JPA repository → `adapter/out/persistence/`
7. Persistence adapter → `adapter/out/persistence/`
8. Request/Response DTOs → `adapter/in/web/dto/`
9. Controller → `adapter/in/web/`
10. Tests → `test/.../`

## Per-Task Implementation Order (Frontend)

1. TypeScript types → `src/types/`
2. API service → `src/api/`
3. Custom hook → `src/hooks/`
4. Components → `src/components/<feature>/`
5. Page → `src/pages/<feature>/`
6. Route registration → `src/App.tsx`
