# TASK-4.25 — In-Port, Service, JPA, Adapter & Controller: Shares

## Overview

Implement the complete vertical slice for the Share feature: in-port use case, domain service, JPA entity & repository, persistence adapter, and REST controller. Sharing is intentionally simple for Phase 4 — no complex business rules, just record creation.

## Requirements

- Follow the same layered patterns established in Phase 3 and TASK-4.22.
- For `DM` shares, the actual message delivery is out of scope for Phase 4 (deferred to Phase 7). Just persist the record.

---

## Checklist

### `SharePostUseCase.java` (In-Port)

- [ ] Create in-port interface:
  ```java
  public interface SharePostUseCase {
      PostShare share(Command command);

      record Command(
          UUID postId,
          UUID sharerId,
          UUID recipientId,    // nullable — only for DM shares
          ShareType shareType
      ) {}
  }
  ```

---

### `ShareService.java` (Domain Service)

- [ ] Create `ShareService.java` annotated with `@Service`
- [ ] Inject via constructor: `ShareRepository`
- [ ] Implement `SharePostUseCase`:
  - [ ] Create `PostShare.of(command.postId(), command.sharerId(), command.recipientId(), command.shareType())`
  - [ ] Call `shareRepository.save(share)` and return it
  - [ ] Log a message if `shareType == DM` noting that DM delivery is pending Phase 7

---

### `PostShareJpaEntity.java` (JPA Entity)

- [ ] Create `PostShareJpaEntity.java`:
  ```java
  @Entity
  @Table(name = "post_shares")
  public class PostShareJpaEntity {

      @Id
      @Column(name = "id", updatable = false, nullable = false)
      private UUID id;

      @Column(name = "post_id", nullable = false)
      private UUID postId;

      @Column(name = "sharer_id", nullable = false)
      private UUID sharerId;

      @Column(name = "recipient_id")
      private UUID recipientId;   // nullable

      @Enumerated(EnumType.STRING)
      @Column(name = "share_type", nullable = false, length = 10)
      private ShareType shareType;

      @Column(name = "created_at", nullable = false, updatable = false)
      @CreationTimestamp
      private Instant createdAt;

      // no-arg constructor, all-args constructor, getters, toDomain(), fromDomain()
  }
  ```

---

### `PostShareJpaRepository.java`

- [ ] Create repository:
  ```java
  public interface PostShareJpaRepository extends JpaRepository<PostShareJpaEntity, UUID> {
      List<PostShareJpaEntity> findByPostId(UUID postId);
  }
  ```

---

### `SharePersistenceAdapter.java`

- [ ] Create `SharePersistenceAdapter.java` annotated with `@Component`
- [ ] Implement `ShareRepository`
- [ ] Inject: `PostShareJpaRepository`
- [ ] Implement `save(PostShare share)`:
  - Map to `PostShareJpaEntity.fromDomain(share)`, save, map back to `toDomain()`
- [ ] Implement `findByPostId(UUID postId)`:
  - Delegate to `postShareJpaRepository.findByPostId(postId)`, map to domain list

---

### `ShareController.java` (REST Controller)

- [ ] Annotate with `@RestController`, `@RequiredArgsConstructor`, `@Tag(name = "Shares")`
- [ ] Inject: `SharePostUseCase`

- [ ] Implement `POST /api/v1/posts/{id}/share`:
  - `@PreAuthorize("isAuthenticated()")`
  - Request body:
    ```java
    public record ShareRequest(
        @NotNull ShareType shareType,
        UUID recipientId    // optional — required only for DM
    ) {}
    ```
  - Calls `sharePostUseCase.share(new Command(id, currentUserId(), request.recipientId(), request.shareType()))`
  - Returns `201 Created` with `ShareResponse`:
    ```java
    public record ShareResponse(UUID id, UUID postId, UUID sharerId, ShareType shareType, Instant createdAt) {
        public static ShareResponse from(PostShare share) { ... }
    }
    ```

- [ ] Add Swagger `@Operation` annotation

---

### Tests (optional for Phase 4 — include if time permits)

- [ ] **`ShareServiceTest.java`**:
  - `share_linkType_savesShareRecord()`
  - `share_dmType_savesShareRecordWithRecipientId()`

- [ ] **`ShareControllerTest.java`**:
  - `POST /posts/{id}/share — 201 Created` with `@WithMockUser`
  - `POST /posts/{id}/share — 401 Unauthorized`
