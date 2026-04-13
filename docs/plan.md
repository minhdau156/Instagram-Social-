# Social Media Platform — Development Plan

> **Version:** 1.0  
> **Last Updated:** 2026-04-12  
> **Reference BRD:** [Business Requirements Document v1.0](./prd/Business%20Requirements%20Document%20%28BRD%29%20for%20Social%20Media%20Platform.md)  
> **Stack:** Java 21 · Spring Boot 3.x · PostgreSQL 15 · React 18 · Vite · Material UI · Hexagonal Architecture

---

## Table of Contents

1. [Architecture Overview](#1-architecture-overview)
2. [Tech Stack](#2-tech-stack)
3. [Phase 0 — Foundation & Infrastructure](#phase-0--foundation--infrastructure)
4. [Phase 1 — Authentication & User Management](#phase-1--authentication--user-management)
5. [Phase 2 — Posts & Media (Core Content)](#phase-2--posts--media-core-content)
6. [Phase 3 — Social Graph (Follow/Unfollow)](#phase-3--social-graph-followunfollow)
7. [Phase 4 — Social Interactions (Likes, Comments, Saves, Shares)](#phase-4--social-interactions-likes-comments-saves-shares)
8. [Phase 5 — Feed & Discovery](#phase-5--feed--discovery)
9. [Phase 6 — Direct Messaging](#phase-6--direct-messaging)
10. [Phase 7 — Notifications](#phase-7--notifications)
11. [Phase 8 — Search](#phase-8--search)
12. [Phase 9 — Content Moderation & Admin](#phase-9--content-moderation--admin)
13. [Phase 10 — Performance, Security & Polish](#phase-10--performance-security--polish)
14. [Database Tables Mapping](#database-tables-mapping)
15. [Module Dependency Graph](#module-dependency-graph)
16. [Definition of Done](#definition-of-done)

---

## 1. Architecture Overview

The system follows **Hexagonal Architecture (Ports & Adapters)**:

```
src/main/java/com/instagram/
├── domain/                  # Pure business logic (no framework deps)
│   ├── model/               # Entities, Value Objects, Aggregates
│   ├── port/
│   │   ├── in/              # Use-case interfaces (driving ports)
│   │   └── out/             # Repository / service interfaces (driven ports)
│   └── service/             # Domain services implementing in-ports
├── application/             # Application orchestration layer
│   └── usecase/             # Concrete use-case implementations
├── adapter/
│   ├── in/
│   │   └── web/             # REST controllers (Spring MVC)
│   └── out/
│       ├── persistence/     # JPA adapters implementing out-ports
│       └── messaging/       # WebSocket / event adapters
└── infrastructure/
    ├── config/              # Spring config, Security, OpenAPI
    ├── security/            # JWT filter, OAuth2 handlers
    └── storage/             # File storage adapter (S3 / local)
```

**Frontend** structure (React + Vite):

```
frontend/src/
├── api/          # Axios services per domain
├── components/   # Reusable UI components
├── pages/        # Route-level page components
├── hooks/        # Custom React hooks
├── types/        # TypeScript interfaces
└── theme.ts      # MUI theme
```

---

## 2. Tech Stack

| Layer | Technology |
|---|---|
| Backend Language | Java 21 |
| Backend Framework | Spring Boot 3.x |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 15 |
| Auth | Spring Security + JWT + OAuth2 (Google, Facebook) |
| Real-time | Spring WebSocket (STOMP) |
| File Storage | AWS S3 (or MinIO for local dev) |
| API Docs | OpenAPI 3 / Swagger UI |
| Frontend | React 18 + TypeScript + Vite |
| UI Library | Material UI (MUI) v5 |
| State Management | React Query (TanStack Query) |
| Real-time (FE) | SockJS + STOMP.js |
| Containerization | Docker + Docker Compose |
| CI/CD | GitHub Actions |

---

## Phase 0 — Foundation & Infrastructure

**Goal:** Project scaffold, database, local dev environment, CI skeleton.

### Status: 🟡 Partially Done

### Tasks

#### Infrastructure
- [ ] `docker-compose.yml` — PostgreSQL 15, MinIO (S3-compatible), Redis
- [x] `Makefile` with `make dev`, `make test`, `make migrate` targets
- [x] Environment config: `application.yml` profiles (`local`, `test`, `prod`)
- [ ] Flyway migrations — integrate `docs/database/schema.sql` as `V1__initial_schema.sql`
- [ ] GitHub Actions CI: build → test → lint

#### Backend Foundation
- [x] Maven project scaffold (`pom.xml`)
- [x] Hexagonal package structure
- [x] OpenAPI / Swagger config (`OpenApiConfig.java`)
- [ ] Global exception handling (`GlobalExceptionHandler`)
- [ ] `BaseEntity` with audit fields
- [ ] Custom `ApiResponse<T>` wrapper for all endpoints
- [ ] CORS configuration

#### Frontend Foundation
- [x] Vite + React + TypeScript scaffold
- [x] MUI theme (`theme.ts`, `AppShell.tsx`)
- [x] Routing (`App.tsx`)
- [ ] Axios instance with interceptors (auth token injection, 401 redirect)
- [ ] React Query `QueryClient` setup
- [ ] Error boundary component
- [ ] Loading / Skeleton components

---

## Phase 1 — Authentication & User Management

**BRD refs:** FR-001, FR-002, FR-003, FR-004  
**DB tables:** `users`, `user_auth_providers`, `password_reset_tokens`, `user_sessions`, `user_stats`

### Goal
Users can register, log in, manage their profile, and recover their password.

### Backend Tasks

#### Domain
- [ ] `User` entity (domain model)
- [ ] `UserStats` value object
- [ ] `UserPort` (in): `RegisterUserUseCase`, `LoginUseCase`, `UpdateProfileUseCase`, `RecoverPasswordUseCase`
- [ ] `UserRepositoryPort` (out)

#### Application / Use Cases
- [ ] `RegisterUserService` — email/phone registration, password hashing (BCrypt)
- [ ] `LoginService` — credential validation, JWT access + refresh token generation
- [ ] `OAuthLoginService` — Google / Facebook OAuth2 callback handler
- [ ] `RefreshTokenService` — rotate refresh tokens
- [ ] `PasswordRecoveryService` — generate reset token, send email
- [ ] `UpdateProfileService` — update bio, avatar, privacy settings

#### Adapters
- [ ] `UserPersistenceAdapter` (JPA)
- [ ] `UserJpaEntity` + `UserJpaRepository`
- [ ] `AuthController` — `/api/v1/auth/register`, `/api/v1/auth/login`, `/api/v1/auth/refresh`, `/api/v1/auth/logout`, `/api/v1/auth/password-reset`
- [ ] `UserController` — `/api/v1/users/{username}`, `/api/v1/users/me` (GET, PUT)
- [ ] `AvatarUploadAdapter` — upload profile picture to S3/MinIO

#### Security
- [ ] `JwtTokenProvider` (generate / validate JWT)
- [ ] `JwtAuthenticationFilter` (Spring Security filter chain)
- [ ] `OAuth2SuccessHandler`
- [ ] Role-based access: `ROLE_USER`, `ROLE_ADMIN`

### Frontend Tasks
- [ ] `AuthContext` + `useAuth` hook
- [ ] `LoginPage` — email/password form + social login buttons
- [ ] `RegisterPage` — multi-step registration form
- [ ] `ForgotPasswordPage`
- [ ] `ProfilePage` — view/edit own profile
- [ ] `PublicProfilePage` — view other users' profiles
- [ ] Protected route guard component
- [ ] Axios interceptor for token refresh

### API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login, returns JWT pair |
| POST | `/api/v1/auth/refresh` | Refresh access token |
| POST | `/api/v1/auth/logout` | Invalidate refresh token |
| POST | `/api/v1/auth/password-reset/request` | Send reset email |
| POST | `/api/v1/auth/password-reset/confirm` | Set new password |
| GET | `/api/v1/users/me` | Own profile |
| PUT | `/api/v1/users/me` | Update own profile |
| GET | `/api/v1/users/{username}` | Public profile |

---

## Phase 2 — Posts & Media (Core Content)

**BRD refs:** FR-005, FR-006, FR-007, FR-008  
**DB tables:** `posts`, `post_media`, `post_media_edits`, `hashtags`, `post_hashtags`, `mentions`

### Goal
Users can create, edit, delete, and view posts with photo/video attachments, captions, hashtags, and location.

### Backend Tasks

#### Domain
- [ ] `Post` aggregate with `PostMedia` value objects
- [ ] `Hashtag` entity
- [ ] `PostPort` (in): `CreatePostUseCase`, `UpdatePostUseCase`, `DeletePostUseCase`, `GetPostUseCase`
- [ ] `PostRepositoryPort` (out)
- [ ] `MediaStoragePort` (out) — abstraction over S3/MinIO

#### Application / Use Cases
- [ ] `CreatePostService` — upload media, extract hashtags from caption, create mentions
- [ ] `UpdatePostService` — update caption/hashtags, edit media metadata
- [ ] `DeletePostService` — soft delete, decrement `user_stats.post_count`
- [ ] `GetPostService` — return post with media, like/comment counts

#### Adapters
- [ ] `PostPersistenceAdapter` (JPA) ✅ partially done
- [ ] `PostJpaEntity`, `PostMediaJpaEntity`, `HashtagJpaEntity`
- [ ] `S3MediaStorageAdapter` — multipart upload to MinIO/S3
- [ ] `PostController` — CRUD endpoints
- [ ] `MediaController` — pre-signed upload URL endpoint

#### File Upload Strategy
- Backend generates a **pre-signed PUT URL** → frontend uploads directly to S3
- After upload, frontend calls `POST /api/v1/posts` with media metadata

### Frontend Tasks
- [x] `PostForm` component (partially done)
- [ ] Image / video file picker with preview
- [ ] Drag & drop multi-file upload
- [ ] Filter & crop editor (client-side: `fabric.js` or `react-easy-crop`)
- [ ] Caption editor with `#hashtag` and `@mention` highlighting
- [ ] Location picker (optional, text input)
- [ ] `PostCard` component — single post display
- [ ] `PostDetailModal` — full post view with comments panel
- [ ] `PostGrid` — 3-column grid for profile pages

### API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/posts` | Create post |
| GET | `/api/v1/posts/{id}` | Get single post |
| PUT | `/api/v1/posts/{id}` | Update caption/location |
| DELETE | `/api/v1/posts/{id}` | Soft-delete post |
| GET | `/api/v1/users/{username}/posts` | User's post grid |
| POST | `/api/v1/media/upload-url` | Get pre-signed upload URL |

---

## Phase 3 — Social Graph (Follow/Unfollow)

**BRD refs:** FR-0016  
**DB tables:** `follows`, `user_stats`

### Goal
Users can follow/unfollow others; private accounts have follow request approval flow.

### Backend Tasks
- [ ] `Follow` domain entity
- [ ] `FollowPort` (in): `FollowUserUseCase`, `UnfollowUserUseCase`, `GetFollowersUseCase`, `GetFollowingUseCase`, `ApproveFollowRequestUseCase`
- [ ] `FollowService` — handle public vs. private account logic
- [ ] `FollowPersistenceAdapter`
- [ ] `FollowController` — `/api/v1/users/{username}/follow`, `/api/v1/users/{username}/unfollow`
- [ ] `user_stats` counter update (triggers or application-level)

### Frontend Tasks
- [ ] `FollowButton` component (states: Follow / Requested / Following / Unfollow)
- [ ] `FollowersList` / `FollowingList` dialogs
- [ ] `FollowRequestsPage` (for private accounts)

### API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/users/{username}/follow` | Follow user |
| DELETE | `/api/v1/users/{username}/follow` | Unfollow user |
| GET | `/api/v1/users/{username}/followers` | Follower list |
| GET | `/api/v1/users/{username}/following` | Following list |
| GET | `/api/v1/follow-requests` | Pending requests (private accounts) |
| POST | `/api/v1/follow-requests/{id}/approve` | Approve request |
| DELETE | `/api/v1/follow-requests/{id}` | Decline request |

---

## Phase 4 — Social Interactions (Likes, Comments, Saves, Shares)

**BRD refs:** FR-0012, FR-0013, FR-0014, FR-0015  
**DB tables:** `post_likes`, `comments`, `comment_likes`, `saved_posts`, `post_shares`

### Goal
Users can like, comment, reply, save, and share posts.

### Backend Tasks

#### Likes
- [ ] `LikePort` (in): `LikePostUseCase`, `UnlikePostUseCase`, `LikeCommentUseCase`
- [ ] `LikePersistenceAdapter` — toggle like, update `posts.like_count`
- [ ] `LikeController`

#### Comments
- [ ] `Comment` entity with nested replies (`parent_id`)
- [ ] `CommentPort` (in): `AddCommentUseCase`, `ReplyCommentUseCase`, `EditCommentUseCase`, `DeleteCommentUseCase`, `GetCommentsUseCase`
- [ ] `CommentService` — also handles `@mention` extraction
- [ ] `CommentPersistenceAdapter`
- [ ] `CommentController`

#### Saves
- [ ] `SavedPostPort` (in): `SavePostUseCase`, `UnsavePostUseCase`, `GetSavedPostsUseCase`
- [ ] `SavedPostPersistenceAdapter`
- [ ] `SaveController`

#### Shares
- [ ] `SharePort` (in): `SharePostUseCase`
- [ ] `SharePersistenceAdapter`
- [ ] `ShareController`

### Frontend Tasks
- [ ] `LikeButton` with animated heart + optimistic update
- [ ] `LikeCount` with liker avatars tooltip
- [ ] `CommentSection` component — threaded comments
- [ ] `CommentInput` with `@mention` autocomplete
- [ ] `SaveButton` (bookmark icon)
- [ ] `SavedPostsPage`
- [ ] `ShareMenu` — in-app share (send to DM) + copy link

### API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/posts/{id}/like` | Like post |
| DELETE | `/api/v1/posts/{id}/like` | Unlike post |
| GET | `/api/v1/posts/{id}/comments` | Get comments (paginated) |
| POST | `/api/v1/posts/{id}/comments` | Add comment |
| PUT | `/api/v1/comments/{id}` | Edit comment |
| DELETE | `/api/v1/comments/{id}` | Delete comment |
| POST | `/api/v1/comments/{id}/like` | Like comment |
| POST | `/api/v1/posts/{id}/save` | Save post |
| DELETE | `/api/v1/posts/{id}/save` | Unsave post |
| GET | `/api/v1/users/me/saved` | Saved posts |
| POST | `/api/v1/posts/{id}/share` | Share post |

---

## Phase 5 — Feed & Discovery

**BRD refs:** FR-009, FR-010  
**DB tables:** `follows`, `user_interests`, `hashtag_stats`

### Goal
Personalized home feed from followed accounts + Explore/Discover page with trending content.

### Backend Tasks

#### Feed
- [ ] `FeedPort` (in): `GetHomeFeedUseCase`, `GetExploreFeedUseCase`
- [ ] `FeedService` — cursor-based pagination, ranking by `created_at` (v1: chronological)
- [ ] Feed query: posts from followed users ordered by recency with keyset pagination
- [ ] Cache feed page 1 in **Redis** (TTL 60s)

#### Discovery / Explore
- [ ] `ExploreService` — trending hashtags (`hashtag_stats.weekly_count`), popular posts
- [ ] `user_interests` signal tracking (passively updated on like/comment/save)

### Frontend Tasks
- [ ] `HomePage` — infinite scroll feed with `IntersectionObserver`
- [ ] `ExplorePage` — mosaic grid, trending hashtags panel
- [ ] `PostSkeleton` loading placeholder
- [ ] Pull-to-refresh gesture

### API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/feed` | Home feed (cursor-paginated) |
| GET | `/api/v1/explore` | Trending/explore posts |
| GET | `/api/v1/explore/hashtags` | Trending hashtags |

---

## Phase 6 — Direct Messaging

**BRD refs:** FR-0017, FR-0018  
**DB tables:** `conversations`, `conversation_members`, `messages`, `message_reads`

### Goal
Real-time 1-to-1 and group messaging with text, image, video, and post-share message types.

### Backend Tasks

#### Domain
- [ ] `Conversation` aggregate, `Message` entity, `ConversationMember` value object
- [ ] `MessagingPort` (in): `CreateConversationUseCase`, `SendMessageUseCase`, `GetConversationsUseCase`, `GetMessagesUseCase`, `MarkReadUseCase`

#### Real-Time (WebSocket)
- [ ] Spring WebSocket config (STOMP over SockJS)
- [ ] WebSocket broker: `/topic/conversations/{id}` for message events
- [ ] `MessageWebSocketController` — `@MessageMapping("/chat.send")`

#### Persistence
- [ ] `ConversationPersistenceAdapter`, `MessagePersistenceAdapter`
- [ ] `MessageController` — REST for history & conversation list

### Frontend Tasks
- [ ] `InboxPage` — conversation list with unread badge
- [ ] `ChatPage` — message thread with infinite scroll upward
- [ ] `MessageBubble` — text / image / video / post-share variants
- [ ] `NewConversationDialog` — user search to start chat
- [ ] `GroupChatDialog` — create group, add/remove members
- [ ] WebSocket connection management (`useWebSocket` hook)
- [ ] Unread count in navigation badge

### API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/conversations` | List user's conversations |
| POST | `/api/v1/conversations` | Create 1-to-1 or group |
| GET | `/api/v1/conversations/{id}/messages` | Message history |
| POST | `/api/v1/conversations/{id}/messages` | Send message (REST fallback) |
| WS | `/ws/chat` | WebSocket endpoint |
| PUT | `/api/v1/conversations/{id}/read` | Mark messages as read |

---

## Phase 7 — Notifications

**BRD refs:** FR-0019, FR-0020  
**DB tables:** `notifications`, `notification_settings`, `device_tokens`

### Goal
Real-time in-app notifications for likes, comments, follows, mentions, and messages; customizable settings.

### Backend Tasks
- [ ] `Notification` entity
- [ ] `NotificationPort` (in): `GetNotificationsUseCase`, `MarkReadUseCase`, `UpdateSettingsUseCase`
- [ ] `NotificationService` — dispatched as domain events after each interaction use case
- [ ] Spring Application Events or a lightweight event bus
- [ ] WebSocket push: `/user/{userId}/topic/notifications`
- [ ] `FCM / APNs` push notification adapter (device tokens)
- [ ] `NotificationController`

### Frontend Tasks
- [ ] `NotificationDropdown` in app bar with bell icon + unread count
- [ ] `NotificationsPage` — full list grouped by time
- [ ] `NotificationItem` — avatar + action text + timestamp
- [ ] `NotificationSettingsPage` — toggle per notification type

### API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/notifications` | List notifications (paginated) |
| PUT | `/api/v1/notifications/{id}/read` | Mark one as read |
| PUT | `/api/v1/notifications/read-all` | Mark all as read |
| GET | `/api/v1/notifications/settings` | Get settings |
| PUT | `/api/v1/notifications/settings` | Update settings |
| POST | `/api/v1/device-tokens` | Register push token |

---

## Phase 8 — Search

**BRD refs:** FR-0011  
**DB tables:** `hashtags`, `hashtag_stats`, `search_history`, `users`

### Goal
Full-text search for users, hashtags, and posts.

### Backend Tasks
- [ ] `SearchPort` (in): `SearchUsersUseCase`, `SearchHashtagsUseCase`, `SearchPostsUseCase`
- [ ] `SearchService` — PostgreSQL `pg_trgm` ILIKE queries; OR Elasticsearch for v2
- [ ] `SearchHistoryService` — save recent searches per user
- [ ] `SearchController`

### Frontend Tasks
- [ ] `SearchPage` — tabbed: People / Hashtags / Posts
- [ ] `SearchBar` with debounced autocomplete dropdown
- [ ] `RecentSearches` list with clear option
- [ ] `HashtagPage` — posts tagged with a hashtag

### API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/search?q=&type=` | Unified search |
| GET | `/api/v1/search/history` | Recent searches |
| DELETE | `/api/v1/search/history` | Clear history |
| GET | `/api/v1/hashtags/{name}/posts` | Posts by hashtag |

---

## Phase 9 — Content Moderation & Admin

**BRD refs:** NFR-007, Admin User Stories  
**DB tables:** `reports`, `user_blocks`, `audit_logs`

### Goal
Users can report and block accounts/content; admins can review and resolve reports.

### Backend Tasks
- [ ] `Report` entity, `UserBlock` entity
- [ ] `ModerationPort` (in): `ReportContentUseCase`, `BlockUserUseCase`, `UnblockUserUseCase`
- [ ] Admin use cases: `ReviewReportUseCase`, `SuspendUserUseCase`, `DeleteContentUseCase`
- [ ] `AuditLogService` — write to `audit_logs` for every sensitive action
- [ ] `ModerationController`, `AdminController` (`ROLE_ADMIN` guard)
- [ ] Block list filtering: hide blocked users from feed/search

### Frontend Tasks
- [ ] `ReportDialog` — accessible from kebab menu on posts/comments/users
- [ ] `BlockedAccountsPage` — list blocked users
- [ ] **Admin Panel** (separate route `/admin/*`):
  - `AdminReportsPage` — report queue, status filters
  - `AdminUserPage` — suspend/unsuspend users
  - `AdminDashboardPage` — basic stats

### API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/reports` | Submit report |
| POST | `/api/v1/users/{username}/block` | Block user |
| DELETE | `/api/v1/users/{username}/block` | Unblock user |
| GET | `/api/v1/users/me/blocked` | List blocked users |
| GET | `/api/v1/admin/reports` | Admin: list reports |
| PUT | `/api/v1/admin/reports/{id}` | Admin: update report status |
| PUT | `/api/v1/admin/users/{id}/suspend` | Admin: suspend user |

---

## Phase 10 — Performance, Security & Polish

**BRD refs:** NFR-001 → NFR-013

### Goal
Production readiness: performance tuning, security hardening, observability, and test coverage.

### Performance
- [ ] Redis caching for feed page 1, user profiles
- [ ] Database query optimization — review N+1 with JPA `@EntityGraph` / `JOIN FETCH`
- [ ] CDN-backed media URLs (CloudFront or similar)
- [ ] Lazy loading images in frontend (`loading="lazy"` + progressive JPEG/AVIF)
- [ ] Bundle size analysis (`vite-bundle-visualizer`)

### Security
- [ ] HTTPS enforced (TLS termination at load balancer)
- [ ] JWT short expiry (15 min access token / 7 day refresh)
- [ ] Rate limiting (`bucket4j` or API gateway)
- [ ] Input validation on all endpoints (`@Valid`, `ConstraintValidator`)
- [ ] SQL injection prevention (parameterized queries via JPA)
- [ ] OWASP dependency check in CI

### Observability
- [ ] Structured logging with SLF4J + MDC request tracing
- [ ] Actuator endpoints + Micrometer metrics
- [ ] Health check: `/actuator/health`
- [ ] Distributed tracing (Zipkin / OpenTelemetry)

### Testing
- [ ] Unit tests: domain services, use cases (JUnit 5 + Mockito)
- [ ] Integration tests: JPA adapters (`@DataJpaTest`)
- [ ] API tests: controllers (`@SpringBootTest` + MockMvc / RestAssured)
- [ ] Frontend: component tests (Vitest + React Testing Library)
- [ ] E2E: Playwright smoke tests for critical flows
- [ ] ≥80% backend test coverage target

### DevOps
- [ ] `Dockerfile` for backend (multi-stage build)
- [ ] `Dockerfile` for frontend
- [ ] Full `docker-compose.yml` (backend + frontend + PostgreSQL + MinIO + Redis)
- [ ] GitHub Actions: build → test → Docker build → push to registry

---

## Database Tables Mapping

| Phase | Tables |
|---|---|
| 0 | — (schema migration only) |
| 1 | `users`, `user_auth_providers`, `password_reset_tokens`, `user_sessions`, `user_stats` |
| 2 | `posts`, `post_media`, `post_media_edits`, `hashtags`, `post_hashtags`, `mentions` |
| 3 | `follows`, `user_stats` (counter updates) |
| 4 | `post_likes`, `comments`, `comment_likes`, `saved_posts`, `post_shares` |
| 5 | `user_interests`, `hashtag_stats` (read-heavy) |
| 6 | `conversations`, `conversation_members`, `messages`, `message_reads` |
| 7 | `notifications`, `notification_settings`, `device_tokens` |
| 8 | `search_history`, `hashtag_stats` |
| 9 | `reports`, `user_blocks`, `audit_logs` |

---

## Module Dependency Graph

```
Phase 0 (Foundation)
  └── Phase 1 (Auth / Users)
        ├── Phase 2 (Posts & Media)
        │     ├── Phase 3 (Follow Graph)
        │     │     └── Phase 5 (Feed)
        │     └── Phase 4 (Interactions)
        │           ├── Phase 5 (Feed)
        │           └── Phase 7 (Notifications)
        ├── Phase 6 (Messaging)
        │     └── Phase 7 (Notifications)
        ├── Phase 8 (Search)
        └── Phase 9 (Moderation)
              └── Phase 10 (Hardening)
```

---

## Definition of Done

A phase is considered **done** when:

1. All backend endpoints are implemented, tested (unit + integration), and documented in Swagger UI.
2. All frontend pages/components are implemented with loading, error, and empty states.
3. New Flyway migrations are applied and reversible.
4. No P0/P1 bugs outstanding.
5. Test coverage for new code ≥ 80%.
6. Code reviewed and merged to `main`.
7. Feature smoke-tested in local Docker Compose environment.
