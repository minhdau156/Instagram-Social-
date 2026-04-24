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

## 💡 Example

```java
// CreatePostUseCase.java
public interface CreatePostUseCase {
    Post createPost(CreatePostCommand command);

    record CreatePostCommand(
        UUID userId,
        String caption,
        String location,
        List<MediaItem> mediaItems
    ) {
        public CreatePostCommand {
            Objects.requireNonNull(userId, "userId must not be null");
            if (mediaItems == null || mediaItems.isEmpty())
                throw new IllegalArgumentException("Post must have at least one media item");
        }
    }
}

// GenerateUploadUrlUseCase.java
public interface GenerateUploadUrlUseCase {
    UploadUrl generateUploadUrl(GenerateUploadUrlCommand command);

    record GenerateUploadUrlCommand(UUID userId, String filename, String contentType) {}
    record UploadUrl(String presignedUrl, String mediaKey) {}
}
```

## ✅ Checklist

- [x] `CreatePostUseCase.java` — `Command(userId, caption, location, mediaItems: List<MediaItem>)`
- [x] `UpdatePostUseCase.java` — `Command(postId, requesterId, caption, location)`
- [x] `DeletePostUseCase.java` — `Command(postId, requesterId)`
- [x] `GetPostUseCase.java` — `Query(postId, currentUserId)`
- [x] `GetUserPostsUseCase.java` — `Query(targetUsername, currentUserId, cursor, limit)`
- [x] `GenerateUploadUrlUseCase.java` — `Command(userId, filename, contentType)`, returns `UploadUrl(presignedUrl, mediaKey)`
