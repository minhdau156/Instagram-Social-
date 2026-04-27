# TASK-3.3 — Out-Port: FollowRepository

## Overview

Define the `FollowRepository` driven port — a pure Java interface in the domain layer that declares all persistence operations needed by `FollowService`. No JPA or Spring annotations here; the implementation lives in the persistence adapter (TASK-3.8).

## Requirements

- Lives in `domain/port/out/`.
- No framework dependencies — plain Java interface.
- Each method signature maps directly to a query or mutation the persistence adapter must fulfil.
- Use `org.springframework.data.domain.Pageable` for pagination parameters (allowed as it is a thin abstraction, not a JPA/Hibernate concept).

## File Location

```
backend/src/main/java/com/instagram/domain/port/out/FollowRepository.java
```

## Checklist

- [ ] Create `FollowRepository.java` interface with the following methods:

  ```java
  public interface FollowRepository {

      /** Persist a new follow or update an existing one (e.g., PENDING → ACCEPTED). */
      Follow save(Follow follow);

      /** Remove the follow relationship between follower and following. */
      void delete(UUID followerId, UUID followingId);

      /** Look up any existing relationship (regardless of status) between two users. */
      Optional<Follow> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

      /** Return accepted followers of the given user, paginated. */
      List<Follow> findFollowersByUserId(UUID userId, Pageable pageable);

      /** Return accepted accounts the given user follows, paginated. */
      List<Follow> findFollowingByUserId(UUID userId, Pageable pageable);

      /** Return all PENDING follow requests addressed to the given user. */
      List<Follow> findPendingRequestsByFollowingId(UUID followingId);

      /** Count accepted followers of the given user. */
      long countFollowersByUserId(UUID userId);

      /** Count accepted accounts the given user follows. */
      long countFollowingByUserId(UUID userId);
  }
  ```

- [ ] Ensure all UUID and List imports come from `java.util.*` and `java.util.UUID`.
- [ ] Import `org.springframework.data.domain.Pageable` for the paginated methods.
