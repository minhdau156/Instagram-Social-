# TASK-2.11 — REST controllers & DTOs

## 📝 Overview

The goal of this task is to implement the **REST controllers & DTOs** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### API Design
- Use `@RestController` and `@RequestMapping`.
- Implement endpoint methods with correct HTTP verbs (GET, POST, PUT, DELETE).
- Use `@PreAuthorize` if authentication is required.
- Add Swagger annotations (`@Operation`, `@ApiResponse`) for API documentation.
- Map incoming Request DTOs to Use Case Commands, and Domain Models to Response DTOs.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/infrastructure/web/controller/PostController.java
backend/src/main/java/com/instagram/infrastructure/web/controller/MediaController.java
backend/src/main/java/com/instagram/infrastructure/web/dto/CreatePostRequest.java
backend/src/main/java/com/instagram/infrastructure/web/dto/UpdatePostRequest.java
backend/src/main/java/com/instagram/infrastructure/web/dto/PostResponse.java
backend/src/main/java/com/instagram/infrastructure/web/dto/MediaItemRequest.java
backend/src/main/java/com/instagram/infrastructure/web/dto/UploadUrlResponse.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## ✅ Checklist

- [ ] Create `PostController.java`
  - `POST /api/v1/posts`
  - `GET /api/v1/posts/{id}`
  - `PUT /api/v1/posts/{id}`
  - `DELETE /api/v1/posts/{id}`
  - `GET /api/v1/users/{username}/posts`
- [ ] Create `MediaController.java`
  - `POST /api/v1/media/upload-url` — returns `{ presignedUrl, mediaKey }`
- [ ] Create DTOs:
  - `CreatePostRequest.java` — `caption`, `location`, `mediaItems: List<MediaItemRequest>`
  - `UpdatePostRequest.java` — `caption`, `location`
  - `PostResponse.java` — full post with media list, like/comment counts; factory `from(Post, List<PostMedia>)`
  - `MediaItemRequest.java` — `mediaKey`, `mediaType`, `width`, `height`, `duration`, `orderIndex`
  - `UploadUrlResponse.java` — `presignedUrl`, `mediaKey`
