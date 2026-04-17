# TASK-1.8 — JPA Repository: UserJpaRepository

## Overview

Create the Spring Data JPA repository for the `users` table. This is a simple interface that extends `JpaRepository` and adds custom query methods needed by the persistence adapter.

## Requirements

- Extends `JpaRepository<UserJpaEntity, UUID>`.
- Adds derived query methods for all lookups needed by the domain.
- Lives in `adapter/out/persistence/` alongside `UserJpaEntity`.

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/UserJpaRepository.java
```

## Notes

- Spring Data JPA derives the SQL for `findByUsername`, `findByEmail`, etc. from the method name — no `@Query` needed.
- `existsByUsername` and `existsByEmail` are the most efficient way to do uniqueness checks (issues `SELECT EXISTS` in SQL).
- Do **not** expose this interface outside the persistence package — `UserPersistenceAdapter` is the only consumer.

## Checklist

- [x] Create `UserJpaRepository.java`:
  ```java
  public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
      Optional<UserJpaEntity> findByUsername(String username);
      Optional<UserJpaEntity> findByEmail(String email);
      boolean existsByUsername(String username);
      boolean existsByEmail(String email);
  }
  ```

- [x] Confirm return types:
  - [x] `findByUsername` → `Optional<UserJpaEntity>`
  - [x] `findByEmail` → `Optional<UserJpaEntity>`
  - [x] `existsByUsername` → `boolean`
  - [x] `existsByEmail` → `boolean`

- [x] No `@Repository` annotation needed (Spring Data auto-detects it)
  > Fixed: removed stray `@Repository` import that was present in the original draft.

- [x] No custom `@Query` or native SQL required at this stage
