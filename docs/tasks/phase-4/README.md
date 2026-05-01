# Phase 4 — Social Interactions (Likes, Comments, Saves, Shares)

> **Depends on:** Phase 1, Phase 2
> **Blocks:** Phase 5, Phase 7
> **BRD refs:** FR-0012, FR-0013, FR-0014, FR-0015
> **DB tables:** `post_likes`, `comments`, `comment_likes`, `saved_posts`, `post_shares`
> **Branch prefix:** `feat/phase-4-`

---

## Task Index

### Backend — Likes

| Task | Title | Status |
|------|-------|--------|
| [TASK-4.1](TASK-4.1-domain-like.md) | Domain models: PostLike & CommentLike | ⬜ |
| [TASK-4.2](TASK-4.2-domain-exceptions-like.md) | Domain exceptions: Like | ⬜ |
| [TASK-4.3](TASK-4.3-out-port-like-repository.md) | Out-port: LikeRepository | ⬜ |
| [TASK-4.4](TASK-4.4-in-ports-like.md) | In-ports: Like use cases | ⬜ |
| [TASK-4.5](TASK-4.5-domain-service-like.md) | Domain service: LikeService | ⬜ |
| [TASK-4.6](TASK-4.6-jpa-like.md) | JPA entities & repositories: Likes | ⬜ |
| [TASK-4.7](TASK-4.7-persistence-adapter-like.md) | Persistence adapter: LikePersistenceAdapter | ⬜ |
| [TASK-4.8](TASK-4.8-rest-controller-like.md) | REST controller: LikeController | ⬜ |
| [TASK-4.9](TASK-4.9-tests-like.md) | Unit & integration tests: Likes | ⬜ |

### Backend — Comments

| Task | Title | Status |
|------|-------|--------|
| [TASK-4.10](TASK-4.10-domain-comment.md) | Domain model: Comment | ⬜ |
| [TASK-4.11](TASK-4.11-domain-exceptions-comment.md) | Domain exceptions: Comment | ⬜ |
| [TASK-4.12](TASK-4.12-out-port-comment-repository.md) | Out-port: CommentRepository | ⬜ |
| [TASK-4.13](TASK-4.13-in-ports-comment.md) | In-ports: Comment use cases | ⬜ |
| [TASK-4.14](TASK-4.14-domain-service-comment.md) | Domain service: CommentService | ⬜ |
| [TASK-4.15](TASK-4.15-jpa-comment.md) | JPA entity & repository: Comment | ⬜ |
| [TASK-4.16](TASK-4.16-persistence-adapter-comment.md) | Persistence adapter: CommentPersistenceAdapter | ⬜ |
| [TASK-4.17](TASK-4.17-rest-controller-comment.md) | REST controller: CommentController | ⬜ |
| [TASK-4.18](TASK-4.18-dtos-comment.md) | Request / Response DTOs: Comment | ⬜ |
| [TASK-4.19](TASK-4.19-tests-comment.md) | Unit & integration tests: Comments | ⬜ |

### Backend — Saves

| Task | Title | Status |
|------|-------|--------|
| [TASK-4.20](TASK-4.20-domain-saved-post.md) | Domain model & out-port: SavedPost | ⬜ |
| [TASK-4.21](TASK-4.21-in-ports-save.md) | In-ports: Save use cases | ⬜ |
| [TASK-4.22](TASK-4.22-service-jpa-adapter-controller-save.md) | Service, JPA, adapter, controller: Saves | ⬜ |
| [TASK-4.23](TASK-4.23-tests-save.md) | Unit & integration tests: Saves | ⬜ |

### Backend — Shares

| Task | Title | Status |
|------|-------|--------|
| [TASK-4.24](TASK-4.24-domain-share.md) | Domain model & out-port: PostShare | ⬜ |
| [TASK-4.25](TASK-4.25-service-jpa-adapter-controller-share.md) | In-port, service, JPA, adapter, controller: Shares | ⬜ |

### Frontend

| Task | Title | Status |
|------|-------|--------|
| [TASK-4.26](TASK-4.26-typescript-types.md) | TypeScript types: post & comment | ⬜ |
| [TASK-4.27](TASK-4.27-api-services.md) | API services: likes, comments, saves, shares | ⬜ |
| [TASK-4.28](TASK-4.28-custom-hooks.md) | Custom hooks: like, comment, save | ⬜ |
| [TASK-4.29](TASK-4.29-like-components.md) | Like components: LikeButton & LikersTooltip | ⬜ |
| [TASK-4.30](TASK-4.30-comment-components.md) | Comment components: CommentSection, Input, Item | ⬜ |
| [TASK-4.31](TASK-4.31-save-button.md) | Save button component | ⬜ |
| [TASK-4.32](TASK-4.32-share-menu.md) | Share menu component | ⬜ |
| [TASK-4.33](TASK-4.33-saved-posts-page.md) | Saved posts page | ⬜ |
| [TASK-4.34](TASK-4.34-integrate-post-card.md) | Integrate action components into PostCard | ⬜ |
| [TASK-4.35](TASK-4.35-routes.md) | Register routes | ⬜ |

---

## Recommended Implementation Order

```
Backend (Likes):
4.1 → 4.2 → 4.3 → 4.4 → 4.5    (domain layer)
4.6 → 4.7                         (persistence layer)
4.8                               (web layer)
4.9                               (tests)

Backend (Comments):
4.10 → 4.11 → 4.12 → 4.13 → 4.14  (domain layer)
4.15 → 4.16                          (persistence layer)
4.17 → 4.18                          (web layer)
4.19                                 (tests)

Backend (Saves):
4.20 → 4.21 → 4.22 → 4.23

Backend (Shares):
4.24 → 4.25

Frontend:
4.26 → 4.27 → 4.28 → 4.29 → 4.30 → 4.31 → 4.32 → 4.33 → 4.34 → 4.35
```
