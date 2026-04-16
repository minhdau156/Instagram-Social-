# TASK-1.4 — Out-Port: UserRepository

## Overview

Define the `UserRepository` out-port interface — the contract that the domain service (`UserService`) uses to persist and retrieve users. This is a pure Java interface with no JPA or Spring annotations. The concrete implementation lives in the persistence adapter (TASK-1.9).

## Requirements

- Interface lives in `domain/port/out/` — **no** Spring or JPA dependencies.
- All methods operate on domain `User` objects, not JPA entities.
- `findBy*` methods return `Optional<User>` — never `null`.
- `exists*` methods return `boolean`.

## File Location

```
backend/src/main/java/com/instagram/domain/port/out/UserRepository.java
```

## Notes

- This interface is the boundary between the domain and infrastructure. Do not import `JpaRepository` or any Spring class here.
- `save(User)` handles both create and update — the adapter distinguishes via presence of `id` in the JPA layer.
- `deleteById` is intentionally omitted — user deletion is handled via `withDeactivated()` (soft approach via status change).

## Checklist

- [x] Create `UserRepository.java` interface in `domain/port/out/`:
  ```java
  public interface UserRepository {
      User save(User user);
      Optional<User> findById(UUID id);
      Optional<User> findByUsername(String username);
      Optional<User> findByEmail(String email);
      boolean existsByUsername(String username);
      boolean existsByEmail(String email);
  }
  ```

- [x] Confirm the interface has:
  - [x] No `import org.springframework.*` or `import jakarta.persistence.*`
  - [x] All finder methods return `Optional<User>` (not raw `User`)
  - [x] `save` returns `User` (the persisted result, potentially with generated ID)

- [ ] Verify `UserService` (TASK-1.6) will depend only on this interface, not on `UserJpaRepository`
