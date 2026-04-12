# Phase 8 — Search

> **Depends on:** Phase 1, Phase 2  
> **BRD refs:** FR-0011  
> **DB tables:** `hashtags`, `hashtag_stats`, `search_history`, `users`  
> **Branch prefix:** `feat/phase-8-`

---

## Backend

### TASK-8.1 — Domain model: SearchHistory
- [ ] Create `backend/.../domain/model/SearchHistory.java`
  - Fields: `id`, `userId`, `query`, `searchType` (`USER` / `HASHTAG` / `POST`), `searchedAt`

### TASK-8.2 — Out-ports
- [ ] `SearchRepository.java`
  - `searchUsers(String query, Pageable) → List<User>`
  - `searchHashtags(String query, Pageable) → List<Hashtag>`
  - `searchPosts(String query, Pageable) → List<Post>` (full-text on caption)
- [ ] `SearchHistoryRepository.java`
  - `save(SearchHistory)`
  - `findByUserIdOrderBySearchedAtDesc(UUID, Pageable)`
  - `deleteByUserId(UUID)`

### TASK-8.3 — In-ports
- [ ] `SearchUsersUseCase.java` — `Query(q, currentUserId, page, size)`
- [ ] `SearchHashtagsUseCase.java` — `Query(q, page, size)`
- [ ] `SearchPostsUseCase.java` — `Query(q, currentUserId, page, size)`
- [ ] `GetSearchHistoryUseCase.java` — `Query(userId)`
- [ ] `ClearSearchHistoryUseCase.java` — `Command(userId)`
- [ ] `GetPostsByHashtagUseCase.java` — `Query(hashtagName, currentUserId, cursor, limit)`

### TASK-8.4 — Domain service: SearchService
- [ ] Create `backend/.../domain/service/SearchService.java`
  - Implements all in-ports from TASK-8.3
  - Delegates DB search to `SearchRepository`
  - Asynchronously saves each search term to `SearchHistory` (`@Async`)
  - `getPostsByHashtag`: looks up `Hashtag` by name, queries `PostRepository` via JOIN on `post_hashtags`

### TASK-8.5 — JPA search adapter
- [ ] Create `SearchJpaAdapter.java` — `implements SearchRepository`
  - User search: `SELECT u FROM UserJpaEntity u WHERE u.username ILIKE :q OR u.fullName ILIKE :q` (native `pg_trgm` `%` pattern)
  - Hashtag search: exact prefix match + `pg_trgm` similarity
  - Post search: `native @Query` full-text search on `posts.caption` with `ILIKE :query`
- [ ] Create `SearchHistoryJpaRepository.java` — standard `JpaRepository`
- [ ] Create `SearchHistoryJpaEntity.java` — `@Table(name = "search_history")`
- [ ] Create `SearchHistoryPersistenceAdapter.java` — `implements SearchHistoryRepository`

### TASK-8.6 — REST controller: SearchController
- [ ] Create `SearchController.java`
  - `GET /api/v1/search?q=&type=` — unified search (type: `users`, `hashtags`, `posts`)
  - `GET /api/v1/search/history`
  - `DELETE /api/v1/search/history`
  - `GET /api/v1/hashtags/{name}/posts` — posts by hashtag (cursor-paginated)

### TASK-8.7 — DTOs
- [ ] `SearchResultResponse.java` — discriminated union: `{ type, users?, hashtags?, posts? }`; alternatively, separate `UserSearchResponse`, `HashtagSearchResponse`, `PostSearchResponse`
- [ ] `SearchHistoryResponse.java` — `id`, `query`, `searchType`, `searchedAt`

### TASK-8.8 — Tests
- [ ] `SearchServiceTest.java` — search users, hashtags, posts; verify history saved (Mockito)
- [ ] `SearchJpaAdapterIT.java` — `@DataJpaTest` with test data
- [ ] `SearchControllerTest.java` — query parameter handling (MockMvc)

---

## Frontend

### TASK-8.9 — TypeScript types
- [ ] Create `frontend/src/types/search.ts`
  - `SearchType`, `SearchResult`, `SearchHistoryItem`

### TASK-8.10 — API services
- [ ] Create `frontend/src/api/searchApi.ts`
  - `search(q, type, page)`, `getSearchHistory()`, `clearSearchHistory()`, `getPostsByHashtag(name, cursor)`

### TASK-8.11 — Custom hooks
- [ ] `useSearch.ts` — debounced `useQuery` (300 ms) keyed on `['search', q, type]`
- [ ] `useSearchHistory.ts` — `useQuery` + `useMutation` for clear
- [ ] `useHashtagPosts.ts` — `useInfiniteQuery` for `getPostsByHashtag`

### TASK-8.12 — Search bar component
- [ ] Create `frontend/src/components/search/SearchBar.tsx`
  - Controlled `<TextField>` with debounce (uses `useSearch`)
  - Dropdown below showing: user avatars w/ username, hashtag chips w/ post count, inline post thumbnails
  - Keyboard navigation (arrow keys + enter)
  - On focus: shows `RecentSearches` list
  - Clears results on Escape

### TASK-8.13 — Recent searches component
- [ ] Create `frontend/src/components/search/RecentSearches.tsx`
  - Lists `SearchHistoryItem`s from `useSearchHistory`
  - Each item: icon (person/tag) + query text + timestamp + X remove button
  - "Clear all" button at top

### TASK-8.14 — Pages
- [ ] Create `frontend/src/pages/search/SearchPage.tsx`
  - MUI `Tabs`: People | Hashtags | Posts
  - Each tab shows paginated results from `useSearch` with appropriate item renderer
  - Empty state: illustration + "No results found for '{q}'"
- [ ] Create `frontend/src/pages/search/HashtagPage.tsx`
  - Header: `#hashtagname` + post count
  - Body: `PostGrid` using `useHashtagPosts`

### TASK-8.15 — Register routes
- [ ] Add `/search` → `SearchPage` (protected)
- [ ] Add `/hashtag/:name` → `HashtagPage` (protected)
- [ ] Integrate `SearchBar` into the app bar / side navigation
