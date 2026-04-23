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

## ✅ Checklist

- [ ] Create `frontend/src/hooks/usePosts.ts` — `useQuery` for `getUserPosts` with cursor pagination
- [ ] Create `frontend/src/hooks/usePost.ts` — `useQuery` for single post
- [ ] Create `frontend/src/hooks/useCreatePost.ts` — `useMutation` wrapping upload + create flow
- [ ] Create `frontend/src/hooks/useDeletePost.ts` — `useMutation` with cache invalidation
