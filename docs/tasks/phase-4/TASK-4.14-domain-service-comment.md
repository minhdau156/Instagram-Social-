# TASK-4.14 — Domain Service: CommentService

## Overview

Implement `CommentService` — the domain service that orchestrates all comment-related use cases. It depends only on **out-port interfaces** (`CommentRepository`, `UserRepository`) and implements all in-ports from TASK-4.13. It also handles `@mention` extraction.

## Requirements

- Annotated with `@Service`.
- Constructor injection only — all dependencies are `final`.
- Never calls JPA repositories directly — only through out-port interfaces.
- Throws domain exceptions (`CommentNotFoundException`, `UnauthorizedCommentAccessException`) on business rule violations.
- Updates `comment_count` on the parent post via `CommentRepository`.

## File Location

```
backend/src/main/java/com/instagram/domain/service/CommentService.java
```

## Dependencies (all are out-port interfaces)

| Field | Interface | Purpose |
|-------|-----------|---------|
| `commentRepository` | `CommentRepository` | Persist / query comments |
| `userRepository` | `UserRepository` | Resolve user for mention extraction |

## Notes

- **Ownership check**: for edit and delete, verify `comment.userId().equals(command.userId())` → throw `UnauthorizedCommentAccessException(commentId, userId)` if not owner.
- **Soft-delete**: call `comment.withSoftDelete()` then `commentRepository.save(...)` — do NOT call `deleteById`.
- **Reply counting**: when adding a reply (`parentId != null`), after saving, call `commentRepository.incrementReplyCount(command.parentId())`.
- **Post comment count**: call `commentRepository.incrementPostCommentCount(postId)` on add; call `decrementPostCommentCount` on delete.
- **`@mention` extraction**: use a simple regex `@(\\w+)` on `content`; for now, just log the mentions (actual notification dispatch belongs to Phase 6).

---

## Checklist

- [ ] Create `CommentService.java` annotated with `@Service`
- [ ] Add constructor accepting `CommentRepository`, `UserRepository`
- [ ] Declare all fields `private final`

- [ ] Implement `AddCommentUseCase`:
  - [ ] Create `Comment.of(command.postId(), command.userId(), command.content(), command.parentId())`
  - [ ] Call `commentRepository.save(comment)`
  - [ ] If `command.parentId() != null`, call `commentRepository.incrementReplyCount(command.parentId())`
  - [ ] Call `commentRepository.incrementPostCommentCount(command.postId())`
  - [ ] Extract `@mentions` with regex and log them (placeholder)
  - [ ] Return saved `Comment`

- [ ] Implement `EditCommentUseCase`:
  - [ ] Load comment: `commentRepository.findById(command.commentId()).orElseThrow(CommentNotFoundException::new)`
  - [ ] Ownership check: `comment.userId().equals(command.userId())` → else throw `UnauthorizedCommentAccessException`
  - [ ] Call `comment.withEdit(command.newContent())`
  - [ ] Save and return updated comment

- [ ] Implement `DeleteCommentUseCase`:
  - [ ] Load comment: `orElseThrow(CommentNotFoundException::new)`
  - [ ] Ownership check → throw `UnauthorizedCommentAccessException` if not owner
  - [ ] Call `comment.withSoftDelete()` then `commentRepository.save(...)`
  - [ ] If `comment.parentId() != null`, call `commentRepository.decrementReplyCount(comment.parentId())`
  - [ ] Call `commentRepository.decrementPostCommentCount(comment.postId())`

- [ ] Implement `GetCommentsUseCase`:
  - [ ] Build `Pageable` from `query.page()` / `query.size()`
  - [ ] Delegate to `commentRepository.findByPostId(query.postId(), pageable)`
  - [ ] Return `Page<Comment>`

- [ ] Implement `GetRepliesUseCase`:
  - [ ] Build `Pageable`
  - [ ] Delegate to `commentRepository.findByParentId(query.commentId(), pageable)`
  - [ ] Return `Page<Comment>`

- [ ] Add private helper `extractMentions(String content)`:
  ```java
  private List<String> extractMentions(String content) {
      Pattern pattern = Pattern.compile("@(\\w+)");
      Matcher matcher = pattern.matcher(content);
      List<String> mentions = new ArrayList<>();
      while (matcher.find()) {
          mentions.add(matcher.group(1));
      }
      return mentions;
  }
  ```
