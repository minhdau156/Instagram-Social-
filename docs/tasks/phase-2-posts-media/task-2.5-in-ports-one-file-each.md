# TASK-2.5 — In-ports (one file each)

## 📝 Overview

The goal of this task is to implement the **In-ports (one file each)** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Interface Design
- **In-Ports:** Define Use Case interfaces (e.g. `CreatePostUseCase`) and nested `Command` or `Query` records for input validation. Command records must use `SelfValidating` traits if applicable.
- **Out-Ports:** Define Repository interfaces (e.g. `PostRepository`) that deal strictly with Domain Models, not JPA Entities.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/CreatePostUseCase.java
backend/src/main/java/com/instagram/UpdatePostUseCase.java
backend/src/main/java/com/instagram/DeletePostUseCase.java
backend/src/main/java/com/instagram/GetPostUseCase.java
backend/src/main/java/com/instagram/GetUserPostsUseCase.java
backend/src/main/java/com/instagram/GenerateUploadUrlUseCase.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## ✅ Checklist

- [ ] `CreatePostUseCase.java` — `Command(userId, caption, location, mediaItems: List<MediaItem>)`
- [ ] `UpdatePostUseCase.java` — `Command(postId, requesterId, caption, location)`
- [ ] `DeletePostUseCase.java` — `Command(postId, requesterId)`
- [ ] `GetPostUseCase.java` — `Query(postId, currentUserId)`
- [ ] `GetUserPostsUseCase.java` — `Query(targetUsername, currentUserId, cursor, limit)`
- [ ] `GenerateUploadUrlUseCase.java` — `Command(userId, filename, contentType)`, returns `UploadUrl(presignedUrl, mediaKey)`
