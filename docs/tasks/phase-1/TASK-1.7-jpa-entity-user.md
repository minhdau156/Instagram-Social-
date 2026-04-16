# TASK-1.7 — JPA Entity: UserJpaEntity

## Overview

Create the `UserJpaEntity` — the JPA representation of the `users` table. This class lives in the persistence adapter layer and is the only place where ORM annotations appear for the user feature.

## Requirements

- Annotated with `@Entity` and `@Table(name = "users")`.
- Extends `BaseJpaEntity` (from TASK-0.8) for `createdAt` / `updatedAt`.
- All columns match `schema.sql` exactly — column names, nullability, uniqueness constraints.
- Lombok `@Getter` / `@Setter` / `@Builder` / `@NoArgsConstructor` / `@AllArgsConstructor` are allowed here.
- Must **not** be referenced outside the `adapter/out/persistence/` package.

## File Location

```
backend/src/main/java/com/instagram/adapter/out/persistence/UserJpaEntity.java
```

## Notes

- Use `@Enumerated(EnumType.STRING)` for the `status` column.
- `username` and `email` need `@Column(unique = true, nullable = false)`.
- `phoneNumber`, `bio`, `avatarUrl`, `passwordHash` are nullable columns.
- Do NOT add bidirectional JPA relationships at this stage — keep it simple.
- The `id` field must be `@GeneratedValue` with `@UuidGenerator` (Hibernate 6 style) since the DB uses `uuid_generate_v4()`.

## Checklist

- [ ] Create `UserJpaEntity.java` extending `BaseJpaEntity`:
  ```java
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Entity
  @Table(name = "users")
  public class UserJpaEntity extends BaseJpaEntity {

      @Id
      @UuidGenerator
      @Column(name = "id", updatable = false, nullable = false)
      private UUID id;

      @Column(name = "username", unique = true, nullable = false, length = 30)
      private String username;

      @Column(name = "email", unique = true, nullable = false)
      private String email;

      @Column(name = "phone_number")
      private String phoneNumber;

      @Column(name = "password_hash")
      private String passwordHash;

      @Column(name = "full_name", nullable = false)
      private String fullName;

      @Column(name = "bio", length = 150)
      private String bio;

      @Column(name = "avatar_url")
      private String avatarUrl;

      @Column(name = "is_private", nullable = false)
      private boolean isPrivate;

      @Column(name = "is_verified", nullable = false)
      private boolean isVerified;

      @Enumerated(EnumType.STRING)
      @Column(name = "status", nullable = false, length = 20)
      private UserStatus status;
  }
  ```

- [ ] Verify column names exactly match `schema.sql` `users` table:
  - [ ] `id`, `username`, `email`, `phone_number`, `password_hash`
  - [ ] `full_name`, `bio`, `avatar_url`, `is_private`, `is_verified`, `status`
  - [ ] `created_at`, `updated_at` inherited from `BaseJpaEntity`

- [ ] Confirm `UserStatus` enum is imported from `domain/model/UserStatus` (shared between domain and JPA)
  > Note: Sharing the enum with the domain layer is acceptable since it's a pure Java enum with no annotations.

- [ ] Write a `@DataJpaTest` `UserJpaRepositoryIT.java` (covered in TASK-1.17, but the entity must compile cleanly first)
