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

## ✅ Checklist

- [ ] Create `PostJpaEntity.java` — `@Entity @Table(name = "posts")`, extends `BaseJpaEntity`
- [ ] Create `PostMediaJpaEntity.java` — `@Entity @Table(name = "post_media")`
- [ ] Create `HashtagJpaEntity.java` — `@Entity @Table(name = "hashtags")`
- [ ] Create `PostHashtagJpaEntity.java` — `@Entity @Table(name = "post_hashtags")` (join table with composite key)
- [ ] Create `MentionJpaEntity.java` — `@Entity @Table(name = "mentions")`
