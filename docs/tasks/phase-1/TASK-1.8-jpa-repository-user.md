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

- [ ] Create `UserJpaRepository.java`:
  ```java
  public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
      Optional<UserJpaEntity> findByUsername(String username);
      Optional<UserJpaEntity> findByEmail(String email);
      boolean existsByUsername(String username);
      boolean existsByEmail(String email);
  }
  ```

- [ ] Confirm return types:
  - [ ] `findByUsername` → `Optional<UserJpaEntity>`
  - [ ] `findByEmail` → `Optional<UserJpaEntity>`
  - [ ] `existsByUsername` → `boolean`
  - [ ] `existsByEmail` → `boolean`

- [ ] No `@Repository` annotation needed (Spring Data auto-detects it)

- [ ] No custom `@Query` or native SQL required at this stage
