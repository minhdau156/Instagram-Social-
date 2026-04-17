# TASK-1.9 — Persistence Adapter: UserPersistenceAdapter

## Overview

Implement `UserPersistenceAdapter` — the bridge between the domain `UserRepository` port and the JPA `UserJpaRepository`. This class is responsible for all mapping between domain objects and JPA entities.

## Requirements

- Implements `UserRepository` (out-port from TASK-1.4).
- Annotated with `@Component` (or `@Repository`).
- Mapping methods (`toEntity` and `toDomain`) are **private**.
- No JPA artifacts (entities, repositories) escape this class.

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/UserPersistenceAdapter.java
```

## Notes

- `save(User)`: map domain `User` → `UserJpaEntity` → call `jpaRepository.save()` → map back to domain `User`.
- When mapping `toDomain`, be careful to preserve `createdAt`/`updatedAt` from the entity (even though the domain `User` doesn't have them, you may need them if the domain model is later extended — for now they are discarded).
- Lombok is allowed in this class if you need it for the private mapping.

## Checklist

- [x] Create `UserPersistenceAdapter.java` implementing `UserRepository`:
  ```java
  @Component
  @RequiredArgsConstructor
  public class UserPersistenceAdapter implements UserRepository {
      private final UserJpaRepository jpaRepository;
      // ...
  }
  ```

- [x] Implement `save(User user)`:
  - [x] Call `toEntity(user)`
  - [x] Call `jpaRepository.save(entity)`
  - [x] Return `toDomain(savedEntity)`

- [x] Implement `findById(UUID id)`:
  - [x] `jpaRepository.findById(id).map(this::toDomain)`

- [x] Implement `findByUsername(String username)`:
  - [x] `jpaRepository.findByUsername(username).map(this::toDomain)`

- [x] Implement `findByEmail(String email)`:
  - [x] `jpaRepository.findByEmail(email).map(this::toDomain)`

- [x] Implement `existsByUsername(String username)`:
  - [x] Delegate to `jpaRepository.existsByUsername(username)`

- [x] Implement `existsByEmail(String email)`:
  - [x] Delegate to `jpaRepository.existsByEmail(email)`

- [x] Implement private `toEntity(User user)` mapping method:
  > Fixed: spec used stale field names (`avatarUrl`, `isPrivate`, `user.password()`).
  > Corrected to: `passwordHash`, `profilePictureUrl`, `websiteUrl`, `privacyLevel`, `phoneNumber`, `isVerified`, `status`, `lastLoginAt`.
  > Added `getLastLoginAt()` getter to `User` domain model (was missing).

- [x] Implement private `toDomain(UserJpaEntity entity)` mapping method:
  > Same field corrections as `toEntity`. All 13 fields mapped.

- [x] Confirm `UserJpaEntity` is never returned from any public method of this class
  > All public methods return `User`, `Optional<User>`, `List<User>`, or `boolean`.

> **Additional fixes applied:**
> - `findAll` return type corrected from `Page<User>` → `List<User>` to match `UserRepository` out-port.
> - `getPassword()` → `getPasswordHash()` (correct getter on `User` and `UserJpaEntity`).
> - All missing imports added (`UserRepository`, `User`, `List`, `Optional`, `UUID`, `PageRequest`).
