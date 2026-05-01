# TASK-4.19 — Unit & Integration Tests: Comments

## Overview

Write a comprehensive test suite for the Comment feature covering domain service logic, persistence adapter database operations, and REST controller HTTP contracts.

## Requirements

- Unit tests use Mockito — no Spring context loaded.
- Persistence integration tests use `@DataJpaTest`.
- Controller tests use `@WebMvcTest` or `@SpringBootTest` + `MockMvc` with `@MockBean` and `@WithMockUser`.
- All test class names end with `Test` (unit) or `IT` (integration test).

## File Locations

```
backend/src/test/java/com/instagram/
├── domain/service/CommentServiceTest.java
├── adapter/out/persistence/CommentPersistenceAdapterIT.java
└── adapter/in/web/CommentControllerTest.java
```

---

## Checklist

### `CommentServiceTest.java` (Unit — Mockito)

- [ ] Mock `CommentRepository`, `UserRepository`
- [ ] Create `CommentService` under test with mocked dependencies

- [ ] **`addComment_topLevel_savesAndIncrementsPostCommentCount()`**:
  - `command.parentId()` is `null`
  - Verify `commentRepository.save(...)` is called with correct content
  - Verify `commentRepository.incrementPostCommentCount(postId)` is called
  - Verify `commentRepository.incrementReplyCount(...)` is **NOT** called

- [ ] **`addComment_reply_savesAndIncrementsReplyAndPostCommentCount()`**:
  - `command.parentId()` is non-null
  - Verify `commentRepository.incrementReplyCount(parentId)` is called
  - Verify `commentRepository.incrementPostCommentCount(postId)` is called

- [ ] **`editComment_owner_updatesContent()`**:
  - `comment.userId()` matches `command.userId()`
  - Verify `commentRepository.save(...)` is called with updated content

- [ ] **`editComment_nonOwner_throwsUnauthorizedCommentAccessException()`**:
  - `comment.userId()` does NOT match `command.userId()`
  - Verify `UnauthorizedCommentAccessException` is thrown
  - Verify `save(...)` is **NOT** called

- [ ] **`deleteComment_owner_softDeletesAndDecrementsCounters()`**:
  - Verify `save(...)` is called with `status = DELETED`
  - Verify `decrementPostCommentCount(postId)` is called
  - Verify `decrementReplyCount(parentId)` is called (when parentId is non-null)

- [ ] **`deleteComment_notFound_throwsCommentNotFoundException()`**:
  - `commentRepository.findById(...)` returns empty
  - Verify `CommentNotFoundException` is thrown

---

### `CommentPersistenceAdapterIT.java` (`@DataJpaTest`)

- [ ] **`save_persistsComment_withCorrectFields()`**:
  - Save a `Comment` and flush; reload via `findById`
  - Assert `content`, `postId`, `userId`, `status`, `likeCount` are correct

- [ ] **`findByPostId_returnsOnlyTopLevelActiveComments()`**:
  - Persist a top-level comment, a reply, and a deleted comment for the same post
  - Assert only the top-level active comment is returned

- [ ] **`findByParentId_returnsActiveRepliesOnly()`**:
  - Persist two active replies and one deleted reply for the same parent
  - Assert only two active replies are returned

- [ ] **`incrementReplyCount_updatesCounterCorrectly()`**:
  - Save comment with `replyCount = 0`; call `incrementReplyCount`
  - Assert `replyCount == 1`

- [ ] **`save_softDeleted_commentHasDeletedStatus()`**:
  - Save a comment, then save with `status = DELETED`
  - Assert `findById` returns comment with `DELETED` status

---

### `CommentControllerTest.java` (MockMvc)

- [ ] **`GET /posts/{id}/comments — 200 OK`**:
  - No auth required
  - Stub `getCommentsUseCase.getComments(...)` to return a page with one comment
  - Assert HTTP 200 and `content` array is present

- [ ] **`POST /posts/{id}/comments — 201 Created`**:
  - Use `@WithMockUser`
  - Stub `addCommentUseCase.addComment(...)` to return a sample `Comment`
  - Assert HTTP 201 and response body contains `content`

- [ ] **`POST /posts/{id}/comments — 400 Bad Request`**:
  - Send empty `content`
  - Assert HTTP 400

- [ ] **`POST /posts/{id}/comments — 401 Unauthorized`**:
  - No authentication
  - Assert HTTP 401 or 403

- [ ] **`PUT /comments/{id} — 200 OK`**:
  - Use `@WithMockUser`; stub returns updated comment
  - Assert HTTP 200

- [ ] **`PUT /comments/{id} — 403 Forbidden`**:
  - Stub throws `UnauthorizedCommentAccessException`
  - Assert HTTP 403

- [ ] **`DELETE /comments/{id} — 204 No Content`**:
  - Use `@WithMockUser`; assert HTTP 204

- [ ] **`GET /comments/{id}/replies — 200 OK`**:
  - No auth required; assert HTTP 200 and list is present
