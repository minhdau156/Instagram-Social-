# TASK-2.7 — JPA entities

## 📝 Overview

The goal of this task is to implement the **JPA entities** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Database Mapping
- Use `@Entity` and `@Table` annotations.
- Extend `BaseJpaEntity` to automatically inherit `createdAt` and `updatedAt` fields.
- Map relationships properly (`@OneToMany`, `@ManyToOne`, etc.) with correct fetch types (prefer `FetchType.LAZY`).
- Ensure columns match `schema.sql` exactly.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/infrastructure/persistence/entity/PostJpaEntity.java
backend/src/main/java/com/instagram/infrastructure/persistence/entity/PostMediaJpaEntity.java
backend/src/main/java/com/instagram/infrastructure/persistence/entity/HashtagJpaEntity.java
backend/src/main/java/com/instagram/infrastructure/persistence/entity/PostHashtagJpaEntity.java
backend/src/main/java/com/instagram/infrastructure/persistence/entity/MentionJpaEntity.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// PostJpaEntity.java
@Entity
@Table(name = "posts")
public class PostJpaEntity extends BaseJpaEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(columnDefinition = "TEXT")
    private String caption;

    @Column(length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    // Getters and setters...
}

// PostHashtagJpaEntity.java — composite key example
@Entity
@Table(name = "post_hashtags")
public class PostHashtagJpaEntity {
    @EmbeddedId
    private PostHashtagId id;

    @Embeddable
    public record PostHashtagId(UUID postId, UUID hashtagId) implements Serializable {}
}
```

## ✅ Checklist

- [ ] Create `PostJpaEntity.java` — `@Entity @Table(name = "posts")`, extends `BaseJpaEntity`
- [ ] Create `PostMediaJpaEntity.java` — `@Entity @Table(name = "post_media")`
- [ ] Create `HashtagJpaEntity.java` — `@Entity @Table(name = "hashtags")`
- [ ] Create `PostHashtagJpaEntity.java` — `@Entity @Table(name = "post_hashtags")` (join table with composite key)
- [ ] Create `MentionJpaEntity.java` — `@Entity @Table(name = "mentions")`
