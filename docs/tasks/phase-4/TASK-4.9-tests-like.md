# TASK-4.9 — Unit & Integration Tests: Likes

## Overview

Write a comprehensive test suite for the Like feature covering domain service logic, persistence adapter database operations, and REST controller HTTP contracts.

## Requirements

- Unit tests use Mockito — no Spring context loaded.
- Persistence integration tests use `@DataJpaTest` — only the JPA slice is loaded.
- Controller tests use `@WebMvcTest` or `@SpringBootTest` + `MockMvc` — with `@MockBean` use cases and `@WithMockUser` for authentication.
- All test class names end with `Test` (unit) or `IT` (integration test) per project convention.

## File Locations

```
backend/src/test/java/com/instagram/
├── domain/service/LikeServiceTest.java
├── adapter/out/persistence/LikePersistenceAdapterIT.java
└── adapter/in/web/LikeControllerTest.java
```

---

## Checklist

### `LikeServiceTest.java` (Unit — Mockito)

- [ ] Mock `LikeRepository`, `PostRepository`, `CommentRepository`, `UserRepository`
- [ ] Create `LikeService` under test with mocked dependencies

- [ ] **`likePost_notYetLiked_savesAndIncrementsCount()`**:
  - `hasLikedPost(...)` returns `false`
  - Verify `likeRepository.likePost(...)` is called
  - Verify `postRepository.incrementLikeCount(...)` is called

- [ ] **`likePost_alreadyLiked_throwsAlreadyLikedException()`**:
  - `hasLikedPost(...)` returns `true`
  - Verify `AlreadyLikedException` is thrown
  - Verify `likeRepository.likePost(...)` is **NOT** called

- [ ] **`unlikePost_liked_deletesAndDecrementsCount()`**:
  - `hasLikedPost(...)` returns `true`
  - Verify `likeRepository.unlikePost(...)` is called
  - Verify `postRepository.decrementLikeCount(...)` is called

- [ ] **`unlikePost_notLiked_throwsNotLikedException()`**:
  - `hasLikedPost(...)` returns `false`
  - Verify `NotLikedException` is thrown

- [ ] **`likeComment_notYetLiked_savesAndIncrementsCount()`**:
  - Same pattern as `likePost` for comment

- [ ] **`likeComment_alreadyLiked_throwsAlreadyLikedException()`**

- [ ] **`unlikeComment_liked_deletesAndDecrementsCount()`**

- [ ] **`getPostLikers_returnsPagedUserSummaries()`**:
  - Stub `findPostLikerIds(...)` to return a list of UUIDs
  - Stub `userRepository.findAllByIds(...)` to return matching user records
  - Assert returned list matches expected usernames

---

### `LikePersistenceAdapterIT.java` (`@DataJpaTest`)

- [ ] Annotate with `@DataJpaTest` and configure `TestEntityManager`

- [ ] **`likePost_persistsLikeRecord()`**:
  - Call `adapter.likePost(postId, userId)` and flush
  - Assert `existsByIdPostIdAndIdUserId(postId, userId)` returns `true`

- [ ] **`unlikePost_removesLikeRecord()`**:
  - Save a like then call `adapter.unlikePost(postId, userId)`
  - Assert record no longer exists

- [ ] **`hasLikedPost_returnsTrueWhenLiked()`**

- [ ] **`hasLikedPost_returnsFalseWhenNotLiked()`**

- [ ] **`findPostLikerIds_returnsUserIdsSortedByCreatedAt()`**:
  - Insert two likes at different timestamps
  - Assert they are returned in descending `createdAt` order

- [ ] **`likeComment_persistsCommentLikeRecord()`**

- [ ] **`hasLikedComment_returnsTrueWhenLiked()`**

---

### `LikeControllerTest.java` (MockMvc)

- [ ] Mock all use-case beans with `@MockBean`
- [ ] Configure `MockMvc` with Spring Security test support

- [ ] **`POST /posts/{id}/like — 204 No Content`**:
  - Use `@WithMockUser`
  - Stub `likePostUseCase.like(...)` to do nothing
  - Assert HTTP 204

- [ ] **`POST /posts/{id}/like — 401 Unauthorized`**:
  - No authentication
  - Assert HTTP 401 or 403

- [ ] **`POST /posts/{id}/like — 409 Conflict`**:
  - Stub `likePostUseCase.like(...)` to throw `AlreadyLikedException`
  - Assert HTTP 409

- [ ] **`DELETE /posts/{id}/like — 204 No Content`**:
  - Use `@WithMockUser`; assert HTTP 204

- [ ] **`DELETE /posts/{id}/like — 404 Not Found`**:
  - Stub to throw `NotLikedException`
  - Assert HTTP 404

- [ ] **`GET /posts/{id}/likers — 200 OK`**:
  - No auth required; stub returns list of user summaries
  - Assert HTTP 200 and list is present in body

- [ ] **`POST /comments/{id}/like — 204 No Content`**

- [ ] **`DELETE /comments/{id}/like — 204 No Content`**
