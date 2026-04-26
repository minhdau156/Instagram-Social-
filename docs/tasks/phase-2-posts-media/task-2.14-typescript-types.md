# TASK-2.14 — TypeScript types

## 📝 Overview

The goal of this task is to implement the **TypeScript types** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

Follow established project patterns to complete this task successfully.

## 📂 File Locations

```text
frontend/src/types/post.ts
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```typescript
// frontend/src/types/post.ts

export type PostStatus = 'draft' | 'published' | 'archived' | 'deleted';
export type MediaType = 'image' | 'video';

export interface PostMedia {
  id: string;
  postId: string;
  mediaType: MediaType;
  mediaUrl: string;
  thumbnailUrl?: string;
  width?: number;
  height?: number;
  duration?: number;   // seconds, video only
  sortOrder: number;
}

export interface Post {
  id: string;
  userId: string;
  caption?: string;
  location?: string;
  status: PostStatus;
  likeCount: number;
  commentCount: number;
  shareCount: number;
  viewCount: number;
  mediaItems: PostMedia[];
  createdAt: string;
  updatedAt: string;
}

export interface CreatePostPayload {
  caption?: string;
  location?: string;
  mediaItems: MediaItem[];
}

export interface MediaItem {
  mediaKey: string;
  mediaType: MediaType;
  width?: number;
  height?: number;
  duration?: number;
  orderIndex: number;
}

export interface UploadUrlResponse {
  presignedUrl: string;
  mediaKey: string;
}
```

## ✅ Checklist

- [x] Create `frontend/src/types/post.ts`
  - `Post`, `PostMedia`, `PostStatus`, `CreatePostPayload`, `UpdatePostPayload`, `MediaItem`, `UploadUrlResponse`
