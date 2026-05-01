# TASK-4.18 — Request / Response DTOs: Comment

## Overview

Create all request and response DTO classes for the Comment feature. These live in the web adapter layer and are the boundary types for HTTP communication. They are never used inside the domain.

## Requirements

- Lives in `adapter/in/web/dto/request/` and `adapter/in/web/dto/response/`.
- Request DTOs use Bean Validation annotations (`@NotBlank`, `@Size`).
- Response DTOs have a static `from(Comment)` factory method — no Lombok.
- Follow the structure of existing DTOs (`FollowResponse`, `UserSummaryResponse`).

## File Locations

```
backend/src/main/java/com/instagram/adapter/in/web/dto/
├── request/
│   ├── AddCommentRequest.java
│   └── EditCommentRequest.java
└── response/
    └── CommentResponse.java
```

---

## Checklist

### `AddCommentRequest.java`

- [ ] Create `AddCommentRequest.java`:
  ```java
  public record AddCommentRequest(
      @NotBlank(message = "Comment content cannot be blank")
      @Size(max = 2200, message = "Comment content cannot exceed 2200 characters")
      String content,

      UUID parentId   // nullable — null means top-level comment
  ) {}
  ```

### `EditCommentRequest.java`

- [ ] Create `EditCommentRequest.java`:
  ```java
  public record EditCommentRequest(
      @NotBlank(message = "Comment content cannot be blank")
      @Size(max = 2200, message = "Comment content cannot exceed 2200 characters")
      String content
  ) {}
  ```

### `CommentResponse.java`

- [ ] Create `CommentResponse.java`:
  ```java
  public record CommentResponse(
      UUID id,
      UUID postId,
      UUID userId,
      String username,
      String avatarUrl,
      UUID parentId,
      String content,
      int likeCount,
      int replyCount,
      CommentStatus status,
      Instant createdAt,
      Instant updatedAt,
      boolean isLikedByCurrentUser  // populated by the service layer; false if unauthenticated
  ) {
      public static CommentResponse from(Comment comment) {
          return new CommentResponse(
              comment.id(),
              comment.postId(),
              comment.userId(),
              null,           // username resolved in service or controller
              null,           // avatarUrl resolved in service or controller
              comment.parentId(),
              comment.content(),
              comment.likeCount(),
              comment.replyCount(),
              comment.status(),
              comment.createdAt(),
              comment.updatedAt(),
              false
          );
      }
  }
  ```

- [ ] Document in a comment that `username`, `avatarUrl`, and `isLikedByCurrentUser` should ideally be populated from an enriched DTO or by the controller/use-case before returning to the client.
- [ ] Consider adding an overloaded `from(Comment, String username, String avatarUrl, boolean isLiked)` factory for the enriched case.
