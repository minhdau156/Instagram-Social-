# TASK-2.15 — API services

## 📝 Overview

The goal of this task is to implement the **API services** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Service Logic
- Implement the defined Use Case interfaces.
- Inject Out-Ports (Repositories, StorageAdapters) via constructor.
- Handle cross-cutting concerns like Hashtag extraction and Mention detection internally or delegate to domain helpers.
- Ensure transactions are NOT managed here (they are managed in the persistence adapter or controller boundary depending on your architecture).

## 📂 File Locations

```text
frontend/src/api/postsApi.ts
frontend/src/api/mediaApi.ts
```

## 🧪 Testing Strategy

- **Unit Tests:** Use JUnit 5 and Mockito to test business logic in isolation.
- **Integration Tests:** Use `@DataJpaTest` for repositories and `@SpringBootTest` with `MockMvc` for controllers.

## ✅ Checklist

- [ ] Create `frontend/src/api/postsApi.ts`
  - `createPost`, `getPostById`, `updatePost`, `deletePost`, `getUserPosts(username, cursor)`
- [ ] Create `frontend/src/api/mediaApi.ts`
  - `getUploadUrl(filename, contentType)` → presigned URL
  - `uploadToS3(presignedUrl, file)` → direct PUT to MinIO
