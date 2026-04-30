# TASK-3.13 — Unit & Integration Tests

## Overview

Write a comprehensive test suite for the social-graph feature covering domain service logic, persistence adapter database operations, and REST controller HTTP contracts.

## Requirements

- Unit tests use Mockito — no Spring context loaded.
- Persistence integration tests use `@DataJpaTest` — only the JPA slice is loaded.
- Controller tests use `@SpringBootTest` + `MockMvc` — full context, with `@WithMockUser` for authentication.
- All test class names end with `Test` (unit) or `IT` (integration test) per project convention.

## File Locations

```
backend/src/test/java/com/instagram/
├── domain/service/FollowServiceTest.java
├── adapter/out/persistence/FollowPersistenceAdapterIT.java
└── adapter/in/web/FollowControllerTest.java
```

---

## Checklist

### `FollowServiceTest.java` (Unit — Mockito)

- [x] Mock `FollowRepository`, `UserRepository`, `UserStatsRepository`
- [x] Create `FollowService` under test with mocked dependencies
- [x] **`follow_publicAccount_createsAcceptedFollow()`**:
  - Target user has `isPrivate = false`
  - Verify `followRepository.save(...)` is called with `status = ACCEPTED`
  - Verify `userStatsRepository.incrementFollowerCount(...)` is called
  - Verify `userStatsRepository.incrementFollowingCount(...)` is called
- [x] **`follow_privateAccount_createsPendingFollow()`**:
  - Target user has `isPrivate = true`
  - Verify `save(...)` is called with `status = PENDING`
  - Verify stats counters are **NOT** incremented
- [x] **`follow_self_throwsCannotFollowYourselfException()`**:
  - `followerId == targetUser.id()`
  - Verify `CannotFollowYourselfException` is thrown
- [x] **`follow_duplicate_throwsAlreadyFollowingException()`**:
  - `followRepository.findByFollowerIdAndFollowingId(...)` returns a non-empty `Optional`
  - Verify `AlreadyFollowingException` is thrown
- [x] **`unfollow_acceptedFollow_deletesAndDecrementsCounters()`**:
  - Existing follow with `ACCEPTED` status found
  - Verify `followRepository.delete(...)` called
  - Verify counters decremented
- [x] **`approveRequest_pendingFollow_acceptsAndIncrementsCounters()`**:
  - Verify `follow.withAccepted()` result is saved
  - Verify counters incremented
- [x] **`declineRequest_pendingFollow_deletesFollow()`**:
  - Verify `followRepository.delete(...)` called; no counter change

---

### `FollowPersistenceAdapterIT.java` (`@DataJpaTest`)

- [x] Annotate with `@DataJpaTest` and configure `TestEntityManager`
- [x] **`save_persistsFollowWithCorrectFields()`**:
  - Save a `Follow` and flush; reload via `findByFollowerIdAndFollowingId`
  - Assert `followerId`, `followingId`, `status`, `createdAt` are correct
- [x] **`delete_removesFollowRelationship()`**:
  - Save then delete; verify `findByFollowerIdAndFollowingId` returns empty
- [x] **`findFollowersByUserId_returnsOnlyAccepted()`**:
  - Persist one `ACCEPTED` and one `PENDING` follow for the same user
  - Assert only the accepted one is returned
- [x] **`findPendingRequestsByFollowingId_returnsPendingOnly()`**:
  - Persist mixed statuses; verify only `PENDING` are returned
- [x] **`userStats_incrementFollowerCount_updatesCorrectly()`**:
  - Set initial count to 5; call `incrementFollowerCount`; assert count is 6

---

### `FollowControllerTest.java` (`@SpringBootTest` + `MockMvc`)

- [x] Mock all use-case beans with `@MockBean`
- [x] Configure `MockMvc` with Spring Security test support
- [x] **`POST /users/{username}/follow — 201 Created`**:
  - Use `@WithMockUser`
  - Stub `followUserUseCase.follow(...)` to return a sample `Follow`
  - Assert HTTP 201 and response body contains `status = "ACCEPTED"`
- [x] **`POST /users/{username}/follow — 401 Unauthorized`**:
  - No authentication header
  - Assert HTTP 401 (or 403 depending on security config)
- [x] **`POST /users/{username}/follow — 409 Conflict`**:
  - Stub use case to throw `AlreadyFollowingException`
  - Assert HTTP 409
- [x] **`DELETE /users/{username}/follow — 204 No Content`**:
  - Use `@WithMockUser`; assert HTTP 204
- [x] **`GET /users/{username}/followers — 200 OK`**:
  - No auth required; stub returns list of summaries
  - Assert HTTP 200 and list is present in body
- [x] **`GET /follow-requests — 200 OK`**:
  - Authenticated user; assert list of pending requests returned
- [x] **`POST /follow-requests/{id}/approve — 200 OK`**:
  - Stub approve use case; assert response with `status = "ACCEPTED"`
- [x] **`DELETE /follow-requests/{id} — 204 No Content`**:
  - Stub decline use case; assert HTTP 204
