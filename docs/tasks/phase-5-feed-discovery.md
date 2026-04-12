# Phase 5 — Feed & Discovery

> **Depends on:** Phase 3 (Follow Graph), Phase 4 (Interactions)  
> **Blocks:** Phase 10 (caching layer)  
> **BRD refs:** FR-009, FR-010  
> **DB tables:** `follows`, `user_interests`, `hashtag_stats`  
> **Branch prefix:** `feat/phase-5-`

---

## Backend — Home Feed

### TASK-5.1 — Out-port: FeedRepository
- [ ] Create `backend/.../domain/port/out/FeedRepository.java`
  - `getHomeFeed(UUID userId, UUID cursor, int limit) → List<Post>`
  - `getExploreFeed(UUID userId, UUID cursor, int limit) → List<Post>`

### TASK-5.2 — In-ports
- [ ] `GetHomeFeedUseCase.java` — `Query(userId, cursor: UUID?, limit: int)`; returns `FeedPage(posts, nextCursor)`
- [ ] `GetExploreFeedUseCase.java` — `Query(userId, cursor: UUID?, limit: int)`

### TASK-5.3 — Domain service: FeedService
- [ ] Create `backend/.../domain/service/FeedService.java`
  - Home feed: keyset-paginated query — posts from users the current user follows, ordered by `created_at DESC`
  - Explore: posts NOT from followed users, ranked by `like_count + comment_count` (descending), exclude already-seen (track with `user_interests` signal)
  - Cache home feed page 1 (cursor = null) in Redis with TTL 60s after implementing Phase 10; leave a `// TODO: add Redis cache` comment for now

### TASK-5.4 — JPA feed query
- [ ] Create `FeedJpaQueryAdapter.java` — `implements FeedRepository`
  - Home feed: JPQL or native SQL using `follows` join:
    ```sql
    SELECT p.* FROM posts p
    JOIN follows f ON f.following_id = p.user_id
    WHERE f.follower_id = :userId
      AND f.status = 'ACCEPTED'
      AND (p.id < :cursor OR :cursor IS NULL)
      AND p.status != 'DELETED'
    ORDER BY p.created_at DESC
    LIMIT :limit
    ```
  - Explore: similar query excluding followed users + sorting by engagement

### TASK-5.5 — REST controller
- [ ] Create `FeedController.java`
  - `GET /api/v1/feed?cursor=&limit=20` → paginated home feed
  - `GET /api/v1/explore?cursor=&limit=20`
  - `GET /api/v1/explore/hashtags?limit=10` → trending hashtag names + post counts

### TASK-5.6 — DTOs
- [ ] `FeedPageResponse.java` — `List<PostResponse> posts`, `String nextCursor`
- [ ] `TrendingHashtagResponse.java` — `name`, `postCount`, `weeklyCount`

### TASK-5.7 — user_interests tracking
- [ ] Create `UserInterestJpaEntity.java` — `@Table(name = "user_interests")`: `userId`, `hashtagId`, `score`, `updatedAt`
- [ ] Create `UserInterestJpaRepository.java`
- [ ] Update `LikeService` and `CommentService` to log interest signals asynchronously (`@Async`)

### TASK-5.8 — hashtag_stats update
- [ ] Create `HashtagStatsJpaEntity.java` — `@Table(name = "hashtag_stats")`: `hashtagId`, `dailyCount`, `weeklyCount`, `updatedAt`
- [ ] Schedule a nightly `@Scheduled` task to roll up daily → weekly counts

### TASK-5.9 — Tests
- [ ] `FeedServiceTest.java` — home feed returns posts from followed users only (Mockito)
- [ ] `FeedJpaQueryAdapterIT.java` — `@DataJpaTest` with test data for follows and posts
- [ ] `FeedControllerTest.java` — cursor pagination correctness (MockMvc)

---

## Frontend

### TASK-5.10 — TypeScript types
- [ ] Update `frontend/src/types/post.ts` — add `FeedPage` type with `posts: Post[]` and `nextCursor: string | null`
- [ ] Create `frontend/src/types/hashtag.ts` — `TrendingHashtag`

### TASK-5.11 — API services
- [ ] Create `frontend/src/api/feedApi.ts`
  - `getHomeFeed(cursor?: string) → Promise<FeedPage>`
  - `getExploreFeed(cursor?: string) → Promise<FeedPage>`
  - `getTrendingHashtags() → Promise<TrendingHashtag[]>`

### TASK-5.12 — Custom hooks
- [ ] Create `frontend/src/hooks/useHomeFeed.ts`
  - `useInfiniteQuery` with `getNextPageParam` from `nextCursor`
  - Refetch on window focus with 60s staleTime
- [ ] Create `frontend/src/hooks/useExploreFeed.ts` — same pattern

### TASK-5.13 — InfiniteScroll utility
- [ ] Create `frontend/src/components/common/InfiniteScroll.tsx`
  - Uses `IntersectionObserver` to call `fetchNextPage` when sentinel element is visible
  - Shows `SkeletonList` while `isFetchingNextPage`

### TASK-5.14 — Home page
- [ ] Create (or update) `frontend/src/pages/feed/HomePage.tsx`
  - Left column: infinite-scroll feed of `PostCard` components using `useHomeFeed`
  - Right column (desktop): suggested users panel (top 5 by follower count — reuse users API)
  - Pull-to-refresh on mobile: call `refetch()` on swipe-down gesture

### TASK-5.15 — Explore page
- [ ] Create `frontend/src/pages/explore/ExplorePage.tsx`
  - Top section: trending hashtags chips (from `getTrendingHashtags`)
  - Main section: mosaic `ImageList` grid with `variant="masonry"` using `useExploreFeed`
  - Clicking a post opens `PostDetailModal`

### TASK-5.16 — Post skeleton
- [ ] Create `frontend/src/components/posts/PostSkeleton.tsx`
  - Mimics the `PostCard` layout using MUI `Skeleton`:
    - Avatar circle + two lines (username + timestamp)
    - Rectangle for image
    - Two short lines for caption

### TASK-5.17 — Register routes
- [ ] Ensure `/` → `HomePage` (protected)
- [ ] Add `/explore` → `ExplorePage` (protected)
