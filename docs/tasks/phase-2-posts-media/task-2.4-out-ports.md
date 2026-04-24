# TASK-2.4 — Out-ports

## 📝 Overview

The goal of this task is to implement the **Out-ports** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Interface Design
- **In-Ports:** Define Use Case interfaces (e.g. `CreatePostUseCase`) and nested `Command` or `Query` records for input validation. Command records must use `SelfValidating` traits if applicable.
- **Out-Ports:** Define Repository interfaces (e.g. `PostRepository`) that deal strictly with Domain Models, not JPA Entities.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/domain/port/out/PostRepository.java
backend/src/main/java/com/instagram/domain/port/out/PostMediaRepository.java
backend/src/main/java/com/instagram/domain/port/out/HashtagRepository.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// PostRepository.java
public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(UUID id);
    Page<Post> findByUserId(UUID userId, Pageable pageable);
    void deleteById(UUID id);
}

// HashtagRepository.java
public interface HashtagRepository {
    Optional<Hashtag> findByName(String name);
    Hashtag save(Hashtag hashtag);
    Hashtag findOrCreate(String name); // find existing or create new
}

// PostMediaRepository.java
public interface PostMediaRepository {
    List<PostMedia> saveAll(List<PostMedia> mediaList);
    List<PostMedia> findByPostId(UUID postId);
}
```

## ✅ Checklist

- [x] Create `backend/.../domain/port/out/PostRepository.java`
  - `save(Post)`, `findById(UUID)`, `findByUserId(UUID, Pageable)`, `deleteById(UUID)`
- [x] Create `backend/.../domain/port/out/PostMediaRepository.java`
  - `saveAll(List<PostMedia>)`, `findByPostId(UUID)`
- [x] Create `backend/.../domain/port/out/HashtagRepository.java`
  - `findByName(String)`, `save(Hashtag)`, `findOrCreate(String name)`
