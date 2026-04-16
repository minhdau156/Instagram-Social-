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

- [ ] Create `UserPersistenceAdapter.java` implementing `UserRepository`:
  ```java
  @Component
  @RequiredArgsConstructor
  public class UserPersistenceAdapter implements UserRepository {
      private final UserJpaRepository jpaRepository;
      // ...
  }
  ```

- [ ] Implement `save(User user)`:
  - [ ] Call `toEntity(user)`
  - [ ] Call `jpaRepository.save(entity)`
  - [ ] Return `toDomain(savedEntity)`

- [ ] Implement `findById(UUID id)`:
  - [ ] `jpaRepository.findById(id).map(this::toDomain)`

- [ ] Implement `findByUsername(String username)`:
  - [ ] `jpaRepository.findByUsername(username).map(this::toDomain)`

- [ ] Implement `findByEmail(String email)`:
  - [ ] `jpaRepository.findByEmail(email).map(this::toDomain)`

- [ ] Implement `existsByUsername(String username)`:
  - [ ] Delegate to `jpaRepository.existsByUsername(username)`

- [ ] Implement `existsByEmail(String email)`:
  - [ ] Delegate to `jpaRepository.existsByEmail(email)`

- [ ] Implement private `toEntity(User user)` mapping method:
  ```java
  private UserJpaEntity toEntity(User user) {
      return UserJpaEntity.builder()
          .id(user.id())
          .username(user.username())
          .email(user.email())
          .phoneNumber(user.phoneNumber())
          .passwordHash(user.passwordHash())
          .fullName(user.fullName())
          .bio(user.bio())
          .avatarUrl(user.avatarUrl())
          .isPrivate(user.isPrivate())
          .isVerified(user.isVerified())
          .status(user.status())
          .build();
  }
  ```

- [ ] Implement private `toDomain(UserJpaEntity entity)` mapping method:
  ```java
  private User toDomain(UserJpaEntity entity) {
      return User.builder()
          .id(entity.getId())
          .username(entity.getUsername())
          .email(entity.getEmail())
          .phoneNumber(entity.getPhoneNumber())
          .passwordHash(entity.getPasswordHash())
          .fullName(entity.getFullName())
          .bio(entity.getBio())
          .avatarUrl(entity.getAvatarUrl())
          .isPrivate(entity.isPrivate())
          .isVerified(entity.isVerified())
          .status(entity.getStatus())
          .build();
  }
  ```

- [ ] Confirm `UserJpaEntity` is never returned from any public method of this class
