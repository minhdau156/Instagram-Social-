# TASK-3.7 — JPA Repository: FollowJpaRepository

## Overview

Create the `FollowJpaRepository` Spring Data JPA interface that provides low-level database access for the `follows` table. This is used exclusively by `FollowPersistenceAdapter` (TASK-3.8) — never called from domain code directly.

## Requirements

- Lives in `adapter/out/persistence/`.
- Extends `JpaRepository<FollowJpaEntity, FollowId>`.
- All query method names must follow Spring Data conventions or use `@Query` for complex cases.
- Pagination-enabled methods accept `Pageable` and return `List<FollowJpaEntity>` (not `Page<>` unless needed).

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/FollowJpaRepository.java
```

## Checklist

- [x] Create `FollowJpaRepository.java` extending `JpaRepository<FollowJpaEntity, FollowId>`:

  ```java
  public interface FollowJpaRepository extends JpaRepository<FollowJpaEntity, FollowId> {

      /** Check/fetch any relationship between two users. */
      Optional<FollowJpaEntity> findByIdFollowerIdAndIdFollowingId(UUID followerId, UUID followingId);

      /** Accepted followers of a user (for followers list). */
      List<FollowJpaEntity> findByIdFollowingIdAndStatus(UUID followingId, FollowStatus status, Pageable pageable);

      /** Accepted accounts a user follows (for following list). */
      List<FollowJpaEntity> findByIdFollowerIdAndStatus(UUID followerId, FollowStatus status, Pageable pageable);

      /** Pending follow requests received by a user, newest first. */
      List<FollowJpaEntity> findByIdFollowingIdAndStatusOrderByCreatedAtDesc(UUID followingId, FollowStatus status);

      /** Hard-delete by composite key. */
      @Modifying
      @Query("DELETE FROM FollowJpaEntity f WHERE f.id.followerId = :followerId AND f.id.followingId = :followingId")
      void deleteByFollowerIdAndFollowingId(@Param("followerId") UUID followerId,
                                            @Param("followingId") UUID followingId);

      /** Count accepted followers. */
      long countByIdFollowingIdAndStatus(UUID followingId, FollowStatus status);

      /** Count accepted following. */
      long countByIdFollowerIdAndStatus(UUID followerId, FollowStatus status);
  }
  ```

- [x] Import `org.springframework.data.jpa.repository.Modifying` and `org.springframework.data.jpa.repository.Query`
- [x] Import `org.springframework.data.repository.query.Param`
- [x] Import `org.springframework.data.domain.Pageable`
