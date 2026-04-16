# Phase 1 — Authentication & User Management

> **Depends on:** Phase 0
> **Blocks:** Phase 2, 3, 6, 8, 9
> **BRD refs:** FR-001, FR-002, FR-003, FR-004
> **DB tables:** `users`, `user_auth_providers`, `password_reset_tokens`, `user_sessions`, `user_stats`
> **Branch prefix:** `feat/phase-1-`

---

## Task Index

### Backend

| Task | Title | Status |
|------|-------|--------|
| [TASK-1.1](TASK-1.1-domain-user.md) | Domain model: User | ⬜ |
| [TASK-1.2](TASK-1.2-domain-user-stats.md) | Domain model: UserStats | ⬜ |
| [TASK-1.3](TASK-1.3-domain-exceptions.md) | Domain exceptions | ⬜ |
| [TASK-1.4](TASK-1.4-out-port-user-repository.md) | Out-port: UserRepository | ⬜ |
| [TASK-1.5](TASK-1.5-in-ports.md) | In-ports (use-case interfaces) | ⬜ |
| [TASK-1.6](TASK-1.6-domain-service-user.md) | Domain service: UserService | ⬜ |
| [TASK-1.7](TASK-1.7-jpa-entity-user.md) | JPA entity: UserJpaEntity | ⬜ |
| [TASK-1.8](TASK-1.8-jpa-repository-user.md) | JPA repository: UserJpaRepository | ⬜ |
| [TASK-1.9](TASK-1.9-persistence-adapter-user.md) | Persistence adapter: UserPersistenceAdapter | ⬜ |
| [TASK-1.10](TASK-1.10-security-infrastructure.md) | Security infrastructure (JWT) | ⬜ |
| [TASK-1.11](TASK-1.11-oauth2.md) | OAuth2 (Google / Facebook) | ⬜ |
| [TASK-1.12](TASK-1.12-password-email-adapters.md) | Password hash & email adapters | ⬜ |
| [TASK-1.13](TASK-1.13-rest-controllers.md) | REST controllers | ⬜ |
| [TASK-1.14](TASK-1.14-request-response-dtos.md) | Request / Response DTOs | ⬜ |
| [TASK-1.15](TASK-1.15-avatar-upload.md) | Avatar upload | ⬜ |
| [TASK-1.16](TASK-1.16-exception-handler-auth.md) | GlobalExceptionHandler — auth mappings | ⬜ |
| [TASK-1.17](TASK-1.17-tests.md) | Unit & integration tests | ⬜ |

### Frontend

| Task | Title | Status |
|------|-------|--------|
| [TASK-1.18](TASK-1.18-typescript-types.md) | TypeScript types | ⬜ |
| [TASK-1.19](TASK-1.19-api-services.md) | API services | ⬜ |
| [TASK-1.20](TASK-1.20-auth-context.md) | AuthContext & useAuth hook | ⬜ |
| [TASK-1.21](TASK-1.21-protected-route.md) | Protected route guard | ⬜ |
| [TASK-1.22](TASK-1.22-pages.md) | Auth & profile pages | ⬜ |
| [TASK-1.23](TASK-1.23-routes.md) | Register routes in App.tsx | ⬜ |

---

## Recommended Implementation Order

```
1.1 → 1.2 → 1.3 → 1.4 → 1.5 → 1.6   (domain layer, no deps)
1.7 → 1.8 → 1.9                        (persistence adapter)
1.10 → 1.11 → 1.12                     (security & external adapters)
1.13 → 1.14 → 1.15 → 1.16             (web layer)
1.17                                    (tests — run after all above)
1.18 → 1.19 → 1.20 → 1.21 → 1.22 → 1.23  (frontend in order)
```
