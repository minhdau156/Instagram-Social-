# TASK-4.23 — Unit & Integration Tests: Saves

## Overview

Write a test suite for the Save feature covering service logic, persistence adapter, and REST controller.

## Requirements

- Unit tests: Mockito only.
- Integration tests: `@DataJpaTest`.
- Controller tests: `MockMvc` with `@MockBean` and `@WithMockUser`.

## File Locations

```
backend/src/test/java/com/instagram/
├── domain/service/SavedPostServiceTest.java
├── adapter/out/persistence/SavedPostPersistenceAdapterIT.java
└── adapter/in/web/SaveControllerTest.java
```

---

## Checklist

### `SavedPostServiceTest.java`

- [ ] Mock `SavedPostRepository`

- [ ] **`save_notYetSaved_persistsSavedPost()`**:
  - `existsByPostIdAndUserId(...)` returns `false`
  - Verify `savedPostRepository.save(...)` is called
  - Assert returned `SavedPost` has correct `postId` and `userId`

- [ ] **`save_alreadySaved_throwsAlreadySavedException()`**:
  - `existsByPostIdAndUserId(...)` returns `true`
  - Verify `AlreadySavedException` is thrown; `save(...)` NOT called

- [ ] **`unsave_saved_deletesRecord()`**:
  - `existsByPostIdAndUserId(...)` returns `true`
  - Verify `delete(...)` is called

- [ ] **`unsave_notSaved_throwsNotSavedException()`**:
  - `existsByPostIdAndUserId(...)` returns `false`
  - Verify `NotSavedException` is thrown

- [ ] **`getSavedPosts_returnsPagedResults()`**:
  - Stub `findByUserId(...)` to return a page
  - Assert result has expected content

---

### `SavedPostPersistenceAdapterIT.java`

- [ ] **`save_persistsSavedPostRecord()`**:
  - Call `adapter.save(SavedPost.of(postId, userId))` and flush
  - Assert `existsByPostIdAndUserId(...)` returns `true`

- [ ] **`delete_removesSavedPostRecord()`**:
  - Save then delete; assert no longer exists

- [ ] **`existsByPostIdAndUserId_returnsFalseWhenNotSaved()`**

- [ ] **`findByUserId_returnsSavedPostsOrderedBySavedAtDesc()`**:
  - Insert two records at different timestamps
  - Assert they are returned in descending `savedAt` order

---

### `SaveControllerTest.java`

- [ ] **`POST /posts/{id}/save — 204 No Content`**:
  - `@WithMockUser`; stub returns `SavedPost`; assert HTTP 204

- [ ] **`POST /posts/{id}/save — 401 Unauthorized`**:
  - No auth; assert HTTP 401 or 403

- [ ] **`POST /posts/{id}/save — 409 Conflict`**:
  - Stub throws `AlreadySavedException`; assert HTTP 409

- [ ] **`DELETE /posts/{id}/save — 204 No Content`**:
  - `@WithMockUser`; assert HTTP 204

- [ ] **`GET /users/me/saved — 200 OK`**:
  - `@WithMockUser`; stub returns page; assert HTTP 200

- [ ] **`GET /users/me/saved — 401 Unauthorized`**:
  - No auth; assert HTTP 401
