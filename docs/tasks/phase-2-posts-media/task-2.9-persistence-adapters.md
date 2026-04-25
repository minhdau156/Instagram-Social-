# TASK-2.9 — Persistence adapters

## 📝 Overview

The goal of this task is to implement the **Persistence adapters** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Adapter Responsibilities
- Implement the Out-Port interface.
- Inject the corresponding Spring Data `JpaRepository`.
- Provide private `toEntity(DomainModel)` and `toDomain(JpaEntity)` mapper methods.
- Add `@Component` or `@Repository` to register it as a Spring Bean.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/infrastructure/persistence/adapter/PostPersistenceAdapter.java
backend/src/main/java/com/instagram/infrastructure/persistence/adapter/PostMediaPersistenceAdapter.java
backend/src/main/java/com/instagram/infrastructure/persistence/adapter/HashtagPersistenceAdapter.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// PostPersistenceAdapter.java
@Component
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository jpaRepository;

    public PostPersistenceAdapter(PostJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Post save(Post post) {
        PostJpaEntity entity = toEntity(post);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Post> findById(UUID id) {
        return jpaRepository.findByIdAndStatusNot(id, PostStatus.DELETED)
            .map(this::toDomain);
    }

    // Private mapping methods — no Lombok, no MapStruct needed
    private PostJpaEntity toEntity(Post post) {
        PostJpaEntity e = new PostJpaEntity();
        e.setId(post.getId());
        e.setUserId(post.getUserId());
        e.setCaption(post.getCaption());
        // ... all fields
        return e;
    }

    private Post toDomain(PostJpaEntity e) {
        return Post.builder()
            .id(e.getId())
            .userId(e.getUserId())
            .caption(e.getCaption())
            // ... all fields
            .build();
    }
}
```

## ✅ Checklist

- [ ] Create `PostPersistenceAdapter.java` — `implements PostRepository` with private `toEntity` / `toDomain`
- [ ] Create `PostMediaPersistenceAdapter.java` — `implements PostMediaRepository`
- [ ] Create `HashtagPersistenceAdapter.java` — `implements HashtagRepository`
