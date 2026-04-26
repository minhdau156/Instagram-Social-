# TASK-2.8 — JPA repositories

## 📝 Overview

The goal of this task is to implement the **JPA repositories** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Database Mapping
- Use `@Entity` and `@Table` annotations.
- Extend `BaseJpaEntity` to automatically inherit `createdAt` and `updatedAt` fields.
- Map relationships properly (`@OneToMany`, `@ManyToOne`, etc.) with correct fetch types (prefer `FetchType.LAZY`).
- Ensure columns match `schema.sql` exactly.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/infrastructure/persistence/repository/PostJpaRepository.java
backend/src/main/java/com/instagram/infrastructure/persistence/repository/PostMediaJpaRepository.java
backend/src/main/java/com/instagram/infrastructure/persistence/repository/HashtagJpaRepository.java
backend/src/main/java/com/instagram/infrastructure/persistence/repository/MentionJpaRepository.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// PostJpaRepository.java
public interface PostJpaRepository extends JpaRepository<PostJpaEntity, UUID> {
    // Feed: user's non-deleted published posts, newest first
    Page<PostJpaEntity> findByUserIdAndStatusNot(
        UUID userId, PostStatus status, Pageable pageable);

    // Single post lookup that skips soft-deleted
    Optional<PostJpaEntity> findByIdAndStatusNot(UUID id, PostStatus status);
}

// HashtagJpaRepository.java
public interface HashtagJpaRepository extends JpaRepository<HashtagJpaEntity, UUID> {
    Optional<HashtagJpaEntity> findByName(String name);

    // Top trending hashtags for the Explore page
    Page<HashtagJpaEntity> findTopByOrderByPostCountDesc(Pageable pageable);
}

// PostMediaJpaRepository.java
public interface PostMediaJpaRepository extends JpaRepository<PostMediaJpaEntity, UUID> {
    List<PostMediaJpaEntity> findByPostIdOrderBySortOrderAsc(UUID postId);
}
```

## ✅ Checklist

- [x] `PostJpaRepository.java` — `findByUserIdAndStatusNot(UUID, PostStatus, Pageable)`, `findByIdAndStatusNot(UUID, PostStatus)`
- [x] `PostMediaJpaRepository.java` — `findByPostIdOrderByOrderIndexAsc(UUID)`
- [x] `HashtagJpaRepository.java` — `findByName(String)`, `findTopByOrderByPostCountDesc(Pageable)` (for explore)
- [x] `MentionJpaRepository.java` — `findByPostId(UUID)`, `findByMentionedUserId(UUID, Pageable)`
