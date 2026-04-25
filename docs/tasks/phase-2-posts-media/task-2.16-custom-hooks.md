# TASK-2.16 — Custom hooks

## 📝 Overview

The goal of this task is to implement the **Custom hooks** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

Follow established project patterns to complete this task successfully.

## 📂 File Locations

```text
frontend/src/hooks/usePosts.ts
frontend/src/hooks/usePost.ts
frontend/src/hooks/useCreatePost.ts
frontend/src/hooks/useDeletePost.ts
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```typescript
// frontend/src/hooks/usePosts.ts — paginated feed
export const usePosts = (username: string) => {
  return useInfiniteQuery({
    queryKey: ['posts', username],
    queryFn: ({ pageParam }) => postsApi.getUserPosts(username, pageParam),
    getNextPageParam: (lastPage) => lastPage.nextCursor ?? undefined,
    initialPageParam: undefined,
  });
};

// frontend/src/hooks/useCreatePost.ts — full upload + create flow
export const useCreatePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (data: { file: File; payload: CreatePostPayload }) => {
      // Step 1: Get pre-signed URL from backend
      const { presignedUrl, mediaKey } = await mediaApi.getUploadUrl(
        data.file.name, data.file.type);
      // Step 2: Upload directly to MinIO
      await mediaApi.uploadToMinio(presignedUrl, data.file);
      // Step 3: Commit post with media key
      return postsApi.createPost({ ...data.payload,
        mediaItems: [{ mediaKey, mediaType: 'image', orderIndex: 0 }] });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// frontend/src/hooks/useDeletePost.ts
export const useDeletePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (postId: string) => postsApi.deletePost(postId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['posts'] }),
  });
};
```

## ✅ Checklist

- [ ] Create `frontend/src/hooks/usePosts.ts` — `useQuery` for `getUserPosts` with cursor pagination
- [ ] Create `frontend/src/hooks/usePost.ts` — `useQuery` for single post
- [ ] Create `frontend/src/hooks/useCreatePost.ts` — `useMutation` wrapping upload + create flow
- [ ] Create `frontend/src/hooks/useDeletePost.ts` — `useMutation` with cache invalidation
