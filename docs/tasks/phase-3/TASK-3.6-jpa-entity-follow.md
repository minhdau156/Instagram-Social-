# TASK-3.6 — JPA Entity: FollowJpaEntity

## Overview

Create the `FollowJpaEntity` — the persistence layer representation of the `follows` table. This class maps directly to the database schema and is the bridge between the relational model and the domain `Follow` object.

## Requirements

- Lives in `adapter/out/persistence/`.
- Annotated with `@Entity @Table(name = "follows")`.
- Uses a composite primary key (`follower_id`, `following_id`) via an `@EmbeddedId`.
- `status` stored as a string enum via `@Enumerated(EnumType.STRING)`.
- Extends `BaseJpaEntity` for `createdAt` / `updatedAt` audit fields (if applicable in your project).

## File Locations

```
backend/src/main/java/com/instagram/adapter/out/persistence/
├── FollowJpaEntity.java
└── FollowId.java          ← composite PK class
```

## Notes

- Use `@EmbeddedId` with a separate `FollowId` embeddable for the composite key — this is cleaner than `@IdClass` for query methods.
- Keep `@ManyToOne` references to `UserJpaEntity` for both `follower` and `following` using `insertable = false, updatable = false` on the join columns mapped through `FollowId`.
- The surrogate `id UUID` column (for follow-request references) should be an additional non-PK column, or you may use the composite key and look up requests via `follower_id + following_id`. Choose based on your schema — document your choice in a code comment.
- `createdAt` should be set by the JPA `@CreationTimestamp` or Flyway default.

## Checklist

- [x] Create `FollowId.java` embeddable:
  ```java
  @Embeddable
  public class FollowId implements Serializable {
      @Column(name = "follower_id")
      private UUID followerId;

      @Column(name = "following_id")
      private UUID followingId;

      // no-arg constructor, all-args constructor, equals(), hashCode()
  }
  ```

- [x] Create `FollowJpaEntity.java`:
  ```java
  @Entity
  @Table(name = "follows")
  public class FollowJpaEntity {

      @EmbeddedId
      private FollowId id;

      @ManyToOne(fetch = FetchType.LAZY)
      @MapsId("followerId")
      @JoinColumn(name = "follower_id")
      private UserJpaEntity follower;

      @ManyToOne(fetch = FetchType.LAZY)
      @MapsId("followingId")
      @JoinColumn(name = "following_id")
      private UserJpaEntity following;

      @Column(name = "is_approved", nullable = false)
      private boolean status;

      @Column(name = "created_at", nullable = false, updatable = false)
      @CreationTimestamp
      private Instant createdAt;

      // no-arg constructor, all-args constructor, getters
  }
  ```

- [x] Ensure `FollowStatus` mapping is documented (using boolean `isApproved`)
- [x] Add `equals()` and `hashCode()` delegating to `FollowId`
