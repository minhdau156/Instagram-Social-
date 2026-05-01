# TASK-4.5 — Domain Service: LikeService

## Overview

Implement `LikeService` — the domain service that orchestrates all like use cases for posts and comments. It depends only on **out-port interfaces** (`LikeRepository`, `PostRepository`, `CommentRepository`) and implements all in-ports from TASK-4.4.

## Requirements

- Annotated with `@Service`.
- Constructor injection only — all dependencies are `final`.
- Never calls JPA repositories directly — only through out-port interfaces.
- Throws domain exceptions (`AlreadyLikedException`, `NotLikedException`) on business rule violations.
- Updates `like_count` on `Post` and `Comment` domain aggregates via their respective repositories.

## File Location

```
backend/src/main/java/com/instagram/domain/service/LikeService.java
```

## Dependencies (all are out-port interfaces)

| Field | Interface | Purpose |
|-------|-----------|---------|
| `likeRepository` | `LikeRepository` | Persist / query like relationships |
| `postRepository` | `PostRepository` | Load post to check visibility; update `like_count` |
| `commentRepository` | `CommentRepository` | Load comment; update `comment like_count` |
| `userRepository` | `UserRepository` | Resolve user summaries for likers list |

## Notes

- **Double-like guard**: before saving, call `likeRepository.hasLikedPost(...)` → if `true`, throw `AlreadyLikedException("post", postId)`.
- **Unlike guard**: before deleting, call `likeRepository.hasLikedPost(...)` → if `false`, throw `NotLikedException("post", postId)`.
- **Count update**: use `postRepository.incrementLikeCount(postId)` and `postRepository.decrementLikeCount(postId)` — these are `@Modifying @Query` operations in the JPA repository (do NOT load the full post entity just to update a counter).
- **Visibility check**: the post's author may have a private account. For now, trust that the calling controller has already resolved access; `LikeService` does **not** re-check visibility.
- Same double-like/unlike guard pattern for comments using `likeRepository.hasLikedComment(...)`.

---

## Checklist

- [ ] Create `LikeService.java` annotated with `@Service`
- [ ] Add constructor accepting `LikeRepository`, `PostRepository`, `CommentRepository`, `UserRepository`
- [ ] Declare all fields `private final`

- [ ] Implement `LikePostUseCase`:
  - [ ] Call `likeRepository.hasLikedPost(command.postId(), command.userId())`
  - [ ] If already liked → throw `new AlreadyLikedException("post", command.postId())`
  - [ ] Call `likeRepository.likePost(command.postId(), command.userId())`
  - [ ] Call `postRepository.incrementLikeCount(command.postId())`

- [ ] Implement `UnlikePostUseCase`:
  - [ ] Call `likeRepository.hasLikedPost(command.postId(), command.userId())`
  - [ ] If not liked → throw `new NotLikedException("post", command.postId())`
  - [ ] Call `likeRepository.unlikePost(command.postId(), command.userId())`
  - [ ] Call `postRepository.decrementLikeCount(command.postId())`

- [ ] Implement `LikeCommentUseCase`:
  - [ ] Same guard pattern using `hasLikedComment`
  - [ ] Call `likeRepository.likeComment(command.commentId(), command.userId())`
  - [ ] Call `commentRepository.incrementLikeCount(command.commentId())`

- [ ] Implement `UnlikeCommentUseCase`:
  - [ ] Same guard pattern
  - [ ] Call `likeRepository.unlikeComment(command.commentId(), command.userId())`
  - [ ] Call `commentRepository.decrementLikeCount(command.commentId())`

- [ ] Implement `GetPostLikersUseCase`:
  - [ ] Build `Pageable` from `query.page()` / `query.size()`
  - [ ] Call `likeRepository.findPostLikerIds(query.postId(), pageable)`
  - [ ] Batch-fetch user records via `userRepository.findAllByIds(ids)`
  - [ ] Map to `UserSummary` list — populate `isFollowing` only if `query.requestingUserId() != null`
  - [ ] Return as domain page wrapper
