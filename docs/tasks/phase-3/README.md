# Phase 3 — Social Graph (Follow/Unfollow)

> **Depends on:** Phase 1, Phase 2
> **Blocks:** Phase 5 (Feed)
> **BRD refs:** FR-0016
> **DB tables:** `follows`, `user_stats`
> **Branch prefix:** `feat/phase-3-`

---

## Task Index

### Backend

| Task | Title | Status |
|------|-------|--------|
| [TASK-3.1](TASK-3.1-domain-follow.md) | Domain model: Follow | ⬜ |
| [TASK-3.2](TASK-3.2-domain-exceptions.md) | Domain exceptions | ⬜ |
| [TASK-3.3](TASK-3.3-out-port-follow-repository.md) | Out-port: FollowRepository | ⬜ |
| [TASK-3.4](TASK-3.4-in-ports.md) | In-ports (use-case interfaces) | ⬜ |
| [TASK-3.5](TASK-3.5-domain-service-follow.md) | Domain service: FollowService | ⬜ |
| [TASK-3.6](TASK-3.6-jpa-entity-follow.md) | JPA entity: FollowJpaEntity | ⬜ |
| [TASK-3.7](TASK-3.7-jpa-repository-follow.md) | JPA repository: FollowJpaRepository | ⬜ |
| [TASK-3.8](TASK-3.8-persistence-adapter-follow.md) | Persistence adapter: FollowPersistenceAdapter | ⬜ |
| [TASK-3.9](TASK-3.9-user-stats-jpa.md) | user_stats JPA entity & repository | ⬜ |
| [TASK-3.10](TASK-3.10-rest-controller.md) | REST controller: FollowController | ⬜ |
| [TASK-3.11](TASK-3.11-dtos.md) | Request / Response DTOs | ⬜ |
| [TASK-3.12](TASK-3.12-exception-handler.md) | GlobalExceptionHandler — follow mappings | ⬜ |
| [TASK-3.13](TASK-3.13-tests.md) | Unit & integration tests | ⬜ |

### Frontend

| Task | Title | Status |
|------|-------|--------|
| [TASK-3.14](TASK-3.14-typescript-types.md) | TypeScript types | ⬜ |
| [TASK-3.15](TASK-3.15-api-services.md) | API services | ⬜ |
| [TASK-3.16](TASK-3.16-custom-hooks.md) | Custom hooks | ⬜ |
| [TASK-3.17](TASK-3.17-components.md) | Follow components | ⬜ |
| [TASK-3.18](TASK-3.18-pages.md) | Follow requests page | ⬜ |
| [TASK-3.19](TASK-3.19-integrate-profile.md) | Integrate FollowButton into ProfilePage | ⬜ |
| [TASK-3.20](TASK-3.20-routes.md) | Register routes in App.tsx | ⬜ |

---

## Recommended Implementation Order

```
3.1 → 3.2 → 3.3 → 3.4 → 3.5        (domain layer, no deps)
3.6 → 3.7 → 3.9 → 3.8               (persistence layer)
3.10 → 3.11 → 3.12                   (web layer)
3.13                                  (tests — run after all above)
3.14 → 3.15 → 3.16 → 3.17 → 3.18 → 3.19 → 3.20  (frontend in order)
```
