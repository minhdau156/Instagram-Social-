# TASK-3.9 — user_stats JPA Entity & Repository

## Overview

Create the `UserStatsJpaEntity` and `UserStatsJpaRepository` to support reading and updating denormalized follower/following counters stored in the `user_stats` table. These counters must be updated atomically using `@Modifying` JPQL queries to avoid race conditions.

## Requirements

- Lives in `adapter/out/persistence/`.
- The entity maps to the existing `user_stats` table (already in schema from Phase 1).
- Counter mutations use atomic `UPDATE ... SET count = count ± 1` SQL — never load-modify-save.
- Expose a `UserStatsRepository` out-port so `FollowService` can call it without knowing JPA details.

## File Locations

```
backend/src/main/java/com/instagram/adapter/out/persistence/
├── UserStatsJpaEntity.java
└── UserStatsJpaRepository.java

backend/src/main/java/com/instagram/domain/port/out/
└── UserStatsRepository.java          ← new out-port
```

## Notes

- If `UserStatsJpaEntity` already exists from Phase 1 (for post count tracking), **extend it** rather than creating a duplicate.
- The `UserStatsRepository` out-port only needs increment/decrement methods — reads are done via `UserRepository` which fetches `UserStats` value object (already defined in Phase 1).
- Use `@Modifying(clearAutomatically = true)` to avoid stale L1-cache entries after bulk updates.

## Checklist

### `UserStatsJpaEntity.java` (create or extend)
- [x] Create `UserStatsJpaEntity.java`:
  ```java
  @Entity
  @Table(name = "user_stats")
  public class UserStatsJpaEntity {

      @Id
      @Column(name = "user_id")
      private UUID userId;

      @Column(name = "follower_count", nullable = false)
      private long followerCount;

      @Column(name = "following_count", nullable = false)
      private long followingCount;

      @Column(name = "post_count", nullable = false)
      private long postCount;

      // no-arg constructor, all-args constructor, getters
  }
  ```

### `UserStatsJpaRepository.java`
- [x] Create `UserStatsJpaRepository.java` extending `JpaRepository<UserStatsJpaEntity, UUID>`:
  ```java
  public interface UserStatsJpaRepository extends JpaRepository<UserStatsJpaEntity, UUID> {

      @Modifying(clearAutomatically = true)
      @Query("UPDATE UserStatsJpaEntity s SET s.followerCount = s.followerCount + 1 WHERE s.userId = :userId")
      void incrementFollowerCount(@Param("userId") UUID userId);

      @Modifying(clearAutomatically = true)
      @Query("UPDATE UserStatsJpaEntity s SET s.followerCount = s.followerCount - 1 WHERE s.userId = :userId")
      void decrementFollowerCount(@Param("userId") UUID userId);

      @Modifying(clearAutomatically = true)
      @Query("UPDATE UserStatsJpaEntity s SET s.followingCount = s.followingCount + 1 WHERE s.userId = :userId")
      void incrementFollowingCount(@Param("userId") UUID userId);

      @Modifying(clearAutomatically = true)
      @Query("UPDATE UserStatsJpaEntity s SET s.followingCount = s.followingCount - 1 WHERE s.userId = :userId")
      void decrementFollowingCount(@Param("userId") UUID userId);
  }
  ```

### `UserStatsRepository.java` (out-port)
- [x] Create `domain/port/out/UserStatsRepository.java`:
  ```java
  public interface UserStatsRepository {
      void incrementFollowerCount(UUID userId);
      void decrementFollowerCount(UUID userId);
      void incrementFollowingCount(UUID userId);
      void decrementFollowingCount(UUID userId);
  }
  ```

### `UserStatsPersistenceAdapter.java` (adapter implementation)
- [x] Create `adapter/out/persistence/UserStatsPersistenceAdapter.java`:
  - Annotate with `@Component`
  - Implement `UserStatsRepository`
  - Inject `UserStatsJpaRepository` via constructor
  - Delegate each method to the matching `@Modifying` query
