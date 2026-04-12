# Phase 3 — Social Graph (Follow/Unfollow)

> **Depends on:** Phase 1, Phase 2  
> **Blocks:** Phase 5 (Feed)  
> **BRD refs:** FR-0016  
> **DB tables:** `follows`, `user_stats`  
> **Branch prefix:** `feat/phase-3-`

---

## Backend

### TASK-3.1 — Domain model: Follow
- [ ] Create `backend/.../domain/model/Follow.java`
  - Fields: `id`, `followerId`, `followingId`, `status` (`PENDING` / `ACCEPTED`), `createdAt`
  - Business method: `withAccepted()` → returns new `Follow` with `status = ACCEPTED`
  - No framework annotations

### TASK-3.2 — Domain exceptions
- [ ] Create `backend/.../domain/exception/AlreadyFollowingException.java`
- [ ] Create `backend/.../domain/exception/FollowRequestNotFoundException.java`
- [ ] Create `backend/.../domain/exception/CannotFollowYourselfException.java`

### TASK-3.3 — Out-port: FollowRepository
- [ ] Create `backend/.../domain/port/out/FollowRepository.java`
  - `save(Follow)`, `delete(UUID followerId, UUID followingId)`
  - `findByFollowerIdAndFollowingId(UUID, UUID) → Optional<Follow>`
  - `findFollowersByUserId(UUID, Pageable) → List<Follow>`
  - `findFollowingByUserId(UUID, Pageable) → List<Follow>`
  - `findPendingRequestsByFollowingId(UUID) → List<Follow>` (for private accounts)
  - `countFollowersByUserId(UUID) → long`
  - `countFollowingByUserId(UUID) → long`

### TASK-3.4 — In-ports (one file each)
- [ ] `FollowUserUseCase.java` — `Command(followerId, targetUsername)`; returns `Follow`
- [ ] `UnfollowUserUseCase.java` — `Command(followerId, targetUsername)`
- [ ] `GetFollowersUseCase.java` — `Query(targetUsername, currentUserId, page, size)`
- [ ] `GetFollowingUseCase.java` — `Query(targetUsername, currentUserId, page, size)`
- [ ] `ApproveFollowRequestUseCase.java` — `Command(followingId, followRequestId)`
- [ ] `DeclineFollowRequestUseCase.java` — `Command(followingId, followRequestId)`
- [ ] `GetFollowRequestsUseCase.java` — `Query(userId)`

### TASK-3.5 — Domain service: FollowService
- [ ] Create `backend/.../domain/service/FollowService.java`
  - Implements all in-ports from TASK-3.4
  - Public account: `follow()` → creates `Follow` with `ACCEPTED` status immediately
  - Private account: `follow()` → creates `Follow` with `PENDING` status (needs approval)
  - After follow is accepted: increment `user_stats.follower_count` and `following_count`
  - After unfollow: decrement counts
  - Check `UserRepository.findByUsername()` to determine if target is private

### TASK-3.6 — JPA entity: FollowJpaEntity
- [ ] Create `backend/.../adapter/out/persistence/FollowJpaEntity.java`
  - `@Entity @Table(name = "follows")`
  - `@IdClass` or `@EmbeddedId` for composite primary key (`follower_id`, `following_id`)
  - `status` as `@Enumerated(EnumType.STRING)`
  - `@ManyToOne` references to `UserJpaEntity` for follower and following

### TASK-3.7 — JPA repository: FollowJpaRepository
- [ ] Create `backend/.../adapter/out/persistence/FollowJpaRepository.java`
  - `findByFollowerIdAndFollowingId(UUID, UUID)`
  - `findByFollowingIdAndStatus(UUID, FollowStatus, Pageable)` — accepted followers
  - `findByFollowerIdAndStatus(UUID, FollowStatus, Pageable)` — accepted following
  - `findByFollowingIdAndStatusOrderByCreatedAtDesc(UUID, FollowStatus)` — pending requests
  - `deleteByFollowerIdAndFollowingId(UUID, UUID)`

### TASK-3.8 — Persistence adapter: FollowPersistenceAdapter
- [ ] Create `backend/.../adapter/out/persistence/FollowPersistenceAdapter.java`
  - `implements FollowRepository`
  - Private `toEntity(Follow)` and `toDomain(FollowJpaEntity)` mapping methods
  - Update `user_stats` counters within same transaction (call `UserStatsJpaRepository.incrementFollower(...)`)

### TASK-3.9 — user_stats JPA update
- [ ] Create `UserStatsJpaEntity.java` — `@Entity @Table(name = "user_stats")`
- [ ] Create `UserStatsJpaRepository.java`
  - `@Modifying @Query("UPDATE ... SET follower_count = follower_count + 1 WHERE user_id = :userId")`
  - Similar queries for decrement, and for `following_count`

### TASK-3.10 — REST controller: FollowController
- [ ] Create `backend/.../adapter/in/web/FollowController.java`
  - `POST /api/v1/users/{username}/follow`
  - `DELETE /api/v1/users/{username}/follow`
  - `GET /api/v1/users/{username}/followers` (paginated)
  - `GET /api/v1/users/{username}/following` (paginated)
  - `GET /api/v1/follow-requests`
  - `POST /api/v1/follow-requests/{id}/approve`
  - `DELETE /api/v1/follow-requests/{id}` (decline)

### TASK-3.11 — DTOs
- [ ] `FollowResponse.java` — `followerId`, `followingId`, `status`, `createdAt`
- [ ] `UserSummaryResponse.java` — `username`, `fullName`, `avatarUrl`, `isVerified` (reusable in follower/following lists)

### TASK-3.12 — Register exception mappings
- [ ] Map `AlreadyFollowingException` → `409`
- [ ] Map `FollowRequestNotFoundException` → `404`
- [ ] Map `CannotFollowYourselfException` → `400`

### TASK-3.13 — Tests
- [ ] `FollowServiceTest.java` — follow public, follow private (→ PENDING), unfollow, approve request (Mockito)
- [ ] `FollowPersistenceAdapterIT.java` — `@DataJpaTest`, CRUD and counter updates
- [ ] `FollowControllerTest.java` — `@SpringBootTest` + MockMvc

---

## Frontend

### TASK-3.14 — TypeScript types
- [ ] Create `frontend/src/types/follow.ts`
  - `Follow`, `FollowStatus`, `FollowRequest`, `UserSummary`

### TASK-3.15 — API services
- [ ] Create `frontend/src/api/followApi.ts`
  - `followUser(username)`, `unfollowUser(username)`
  - `getFollowers(username, page)`, `getFollowing(username, page)`
  - `getFollowRequests()`, `approveRequest(id)`, `declineRequest(id)`

### TASK-3.16 — Custom hooks
- [ ] Create `frontend/src/hooks/useFollow.ts`
  - `useMutation` for follow/unfollow with optimistic update on `ProfilePage` stats
- [ ] Create `frontend/src/hooks/useFollowers.ts` — paginated infinite query
- [ ] Create `frontend/src/hooks/useFollowing.ts` — paginated infinite query
- [ ] Create `frontend/src/hooks/useFollowRequests.ts` — query + approve/decline mutations

### TASK-3.17 — Components
- [ ] Create `frontend/src/components/follow/FollowButton.tsx`
  - States: `Follow` → `Requested` (private) → `Following` → hover `Unfollow`
  - Uses `useFollow` hook; shows loading spinner during mutation
- [ ] Create `frontend/src/components/follow/UserListItem.tsx`
  - Reusable: avatar + username + fullName + `FollowButton`
- [ ] Create `frontend/src/components/follow/FollowersDialog.tsx`
  - MUI Dialog with infinite-scroll `UserListItem` list from `useFollowers`
- [ ] Create `frontend/src/components/follow/FollowingDialog.tsx`
  - Same pattern for following list

### TASK-3.18 — Pages
- [ ] Create `frontend/src/pages/follow/FollowRequestsPage.tsx`
  - List of pending follow requests with Accept / Decline buttons
  - Only visible to the current user (protected route)

### TASK-3.19 — Integrate FollowButton into ProfilePage
- [ ] Update `PublicProfilePage.tsx` to show `FollowButton` when viewing another user's profile
- [ ] Update stat counters (followers, following) to link to `FollowersDialog` / `FollowingDialog`

### TASK-3.20 — Register routes
- [ ] Add route `/follow-requests` → `FollowRequestsPage` (protected)
