# TASK-0.8 — BaseEntity Audit Fields

## Overview

Create a `@MappedSuperclass` JPA base entity that automatically populates `created_at` and `updated_at` timestamps on every entity. All JPA entities in the project will extend this class, ensuring consistent audit fields across all tables without duplicating the mapping code.

## Requirements

- Fields: `createdAt` (`OffsetDateTime`) and `updatedAt` (`OffsetDateTime`).
- Timestamps are set automatically by JPA auditing — never set manually in application code.
- `createdAt` is set once on persist and must be immutable thereafter (`updatable = false`).
- `updatedAt` is refreshed on every `merge` operation.
- JPA auditing must be globally enabled.

## Notes

- Location: `adapter/out/persistence/BaseJpaEntity.java` — the base class lives in the persistence adapter layer.
- Use Spring Data JPA's auditing annotations: `@CreatedDate` and `@LastModifiedDate`. These require `@EnableJpaAuditing` on a config class.
- The field type must be `OffsetDateTime` (not `LocalDateTime`) to store timezone-aware timestamps matching the PostgreSQL `TIMESTAMPTZ` columns in the schema.
- JPA auditing config should go in an existing or new `JpaConfig.java` in `infrastructure/config/`.
- Do not use Lombok on this class — use `@Getter` from Lombok only if the project already uses it in the persistence layer. Per coding standards, Lombok is allowed in the `adapter/` layer.

## Checklist

- [x] Create `BaseJpaEntity.java` in `backend/.../adapter/out/persistence/`
  ```java
  @Getter
  @MappedSuperclass
  @EntityListeners(AuditingEntityListener.class)
  public abstract class BaseJpaEntity {

      @CreatedDate
      @Column(name = "created_at", nullable = false, updatable = false)
      private OffsetDateTime createdAt;

      @LastModifiedDate
      @Column(name = "updated_at", nullable = false)
      private OffsetDateTime updatedAt;
  }
  ```
- [x] Create or update `JpaConfig.java` in `backend/.../infrastructure/config/`:
  ```java
  @Configuration
  @EnableJpaAuditing
  public class JpaConfig {}
  ```
- [x] Ensure Jackson knows how to serialize `OffsetDateTime` (covered in TASK-0.7, but double-check here)
- [x] Update any existing JPA entities (e.g., `PostJpaEntity`) to extend `BaseJpaEntity` and remove manually managed `createdAt`/`updatedAt` fields if present
- [x] Write a quick `@DataJpaTest` to verify that `createdAt` and `updatedAt` are populated on save
