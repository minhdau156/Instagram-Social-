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

## 💡 Example

```typescript
// frontend/src/api/postsApi.ts
export const postsApi = {
  createPost: async (payload: CreatePostPayload): Promise<Post> => {
    const { data } = await axiosInstance.post('/api/v1/posts', payload);
    return data.data;
  },

  getPostById: async (id: string): Promise<Post> => {
    const { data } = await axiosInstance.get(`/api/v1/posts/${id}`);
    return data.data;
  },

  updatePost: async (id: string, payload: UpdatePostPayload): Promise<Post> => {
    const { data } = await axiosInstance.put(`/api/v1/posts/${id}`, payload);
    return data.data;
  },

  deletePost: async (id: string): Promise<void> => {
    await axiosInstance.delete(`/api/v1/posts/${id}`);
  },

  getUserPosts: async (username: string, cursor?: string): Promise<Post[]> => {
    const { data } = await axiosInstance.get(`/api/v1/posts/users/${username}/posts`, {
      params: { cursor, limit: 12 }
    });
    return data.data.content;
  },
};

// frontend/src/api/mediaApi.ts
export const mediaApi = {
  getUploadUrl: async (filename: string, contentType: string): Promise<UploadUrlResponse> => {
    const { data } = await axiosInstance.post('/api/v1/media/upload-url', { filename, contentType });
    return data.data;
  },

  // Direct PUT to MinIO — no auth headers needed, use plain fetch/axios
  uploadToMinio: async (presignedUrl: string, file: File): Promise<void> => {
    await fetch(presignedUrl, { method: 'PUT', body: file, headers: { 'Content-Type': file.type } });
  },
};
```

## ✅ Checklist

- [ ] Create `frontend/src/api/postsApi.ts`
  - `createPost`, `getPostById`, `updatePost`, `deletePost`, `getUserPosts(username, cursor)`
- [ ] Create `frontend/src/api/mediaApi.ts`
  - `getUploadUrl(filename, contentType)` → presigned URL
  - `uploadToS3(presignedUrl, file)` → direct PUT to MinIO
