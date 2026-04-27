# TASK-3.8 — Persistence Adapter: FollowPersistenceAdapter

## Overview

Implement `FollowPersistenceAdapter` — the outbound adapter that bridges the domain's `FollowRepository` port with the JPA layer. It translates between `Follow` domain objects and `FollowJpaEntity` instances, and coordinates `user_stats` counter updates within the same transaction.

## Requirements

- Lives in `adapter/out/persistence/`.
- Annotated with `@Component` (or `@Repository`) and `@Transactional` at the class level (or per method where appropriate).
- Implements `FollowRepository` from TASK-3.3.
- Uses `FollowJpaRepository` (TASK-3.7) and `UserStatsJpaRepository` (TASK-3.9) — never domain service or use case.
- Contains private `toEntity(Follow)` and `toDomain(FollowJpaEntity)` mapping methods.

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/FollowPersistenceAdapter.java
```

## Notes

- `save(Follow)`: map to entity → call `jpaRepository.save(entity)` → map back to domain.
- `delete(UUID, UUID)`: call `jpaRepository.deleteByFollowerIdAndFollowingId(...)` — must be `@Transactional`.
- `toEntity(Follow)` must correctly populate `FollowId` with `followerId` and `followingId`.
- `toDomain(FollowJpaEntity)` reads from `entity.getId().getFollowerId()` etc.
- Counter updates (`incrementFollowerCount`, `decrementFollowerCount`, etc.) are **not** done here — they are orchestrated by `FollowService`. The adapter only persists the `Follow` object.

## Checklist

- [ ] Create `FollowPersistenceAdapter.java` annotated with `@Component`
- [ ] Inject `FollowJpaRepository` via constructor
- [ ] Implement `save(Follow follow)`:
  ```java
  @Override
  @Transactional
  public Follow save(Follow follow) {
      FollowJpaEntity entity = toEntity(follow);
      return toDomain(followJpaRepository.save(entity));
  }
  ```

- [ ] Implement `delete(UUID followerId, UUID followingId)`:
  ```java
  @Override
  @Transactional
  public void delete(UUID followerId, UUID followingId) {
      followJpaRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
  }
  ```

- [ ] Implement `findByFollowerIdAndFollowingId(UUID, UUID)`:
  - Delegates to `followJpaRepository.findByIdFollowerIdAndIdFollowingId(...)`
  - Maps result with `toDomain()`

- [ ] Implement `findFollowersByUserId(UUID userId, Pageable pageable)`:
  - Calls `findByIdFollowingIdAndStatus(userId, FollowStatus.ACCEPTED, pageable)`
  - Maps result list to domain

- [ ] Implement `findFollowingByUserId(UUID userId, Pageable pageable)`:
  - Calls `findByIdFollowerIdAndStatus(userId, FollowStatus.ACCEPTED, pageable)`

- [ ] Implement `findPendingRequestsByFollowingId(UUID followingId)`:
  - Calls `findByIdFollowingIdAndStatusOrderByCreatedAtDesc(followingId, FollowStatus.PENDING)`

- [ ] Implement `countFollowersByUserId(UUID userId)`:
  - Calls `countByIdFollowingIdAndStatus(userId, FollowStatus.ACCEPTED)`

- [ ] Implement `countFollowingByUserId(UUID userId)`:
  - Calls `countByIdFollowerIdAndStatus(userId, FollowStatus.ACCEPTED)`

- [ ] Implement private `toEntity(Follow follow)`:
  ```java
  private FollowJpaEntity toEntity(Follow follow) {
      FollowId followId = new FollowId(follow.followerId(), follow.followingId());
      return new FollowJpaEntity(followId, follow.status(), follow.createdAt());
  }
  ```

- [ ] Implement private `toDomain(FollowJpaEntity entity)`:
  ```java
  private Follow toDomain(FollowJpaEntity entity) {
      return new Follow.Builder()
          .id(/* surrogate id or composite */)
          .followerId(entity.getId().getFollowerId())
          .followingId(entity.getId().getFollowingId())
          .status(entity.getStatus())
          .createdAt(entity.getCreatedAt())
          .build();
  }
  ```
