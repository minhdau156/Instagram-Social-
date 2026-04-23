# TASK-2.6 — Domain service: PostService

## 📝 Overview

The goal of this task is to implement the **Domain service: PostService** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Service Logic
- Implement the defined Use Case interfaces.
- Inject Out-Ports (Repositories, StorageAdapters) via constructor.
- Handle cross-cutting concerns like Hashtag extraction and Mention detection internally or delegate to domain helpers.
- Ensure transactions are NOT managed here (they are managed in the persistence adapter or controller boundary depending on your architecture).

## 📂 File Locations

```text
backend/src/main/java/com/instagram/domain/service/PostService.java
```

## 🧪 Testing Strategy

- **Unit Tests:** Use JUnit 5 and Mockito to test business logic in isolation.
- **Integration Tests:** Use `@DataJpaTest` for repositories and `@SpringBootTest` with `MockMvc` for controllers.

## ✅ Checklist

- [ ] Create `backend/.../domain/service/PostService.java`
  - Implements all in-ports from TASK-2.5
  - Hashtag extraction logic: parse `#word` from caption string
  - Mention extraction logic: parse `@username` from caption
  - Delegates media upload URL generation to `MediaStoragePort`
  - Delegates DB persistence to `PostRepository`, `PostMediaRepository`, `HashtagRepository`
