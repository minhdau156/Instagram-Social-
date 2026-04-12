# Phase 4 — Social Interactions (Likes, Comments, Saves, Shares)

> **Depends on:** Phase 1, Phase 2  
> **Blocks:** Phase 5, Phase 7  
> **BRD refs:** FR-0012, FR-0013, FR-0014, FR-0015  
> **DB tables:** `post_likes`, `comments`, `comment_likes`, `saved_posts`, `post_shares`  
> **Branch prefix:** `feat/phase-4-`

---

## Backend — Likes

### TASK-4.1 — Domain model: Like
- [ ] Create `backend/.../domain/model/PostLike.java` — `postId`, `userId`, `createdAt`
- [ ] Create `backend/.../domain/model/CommentLike.java` — `commentId`, `userId`, `createdAt`

### TASK-4.2 — Domain exceptions
- [ ] Create `backend/.../domain/exception/AlreadyLikedException.java`
- [ ] Create `backend/.../domain/exception/NotLikedException.java`

### TASK-4.3 — Out-port: LikeRepository
- [ ] Create `backend/.../domain/port/out/LikeRepository.java`
  - `likePost(UUID postId, UUID userId)`
  - `unlikePost(UUID postId, UUID userId)`
  - `hasLikedPost(UUID postId, UUID userId) → boolean`
  - `likeComment(UUID commentId, UUID userId)`
  - `unlikeComment(UUID commentId, UUID userId)`
  - `hasLikedComment(UUID commentId, UUID userId) → boolean`

### TASK-4.4 — In-ports
- [ ] `LikePostUseCase.java` — `Command(postId, userId)`
- [ ] `UnlikePostUseCase.java` — `Command(postId, userId)`
- [ ] `LikeCommentUseCase.java` — `Command(commentId, userId)`
- [ ] `UnlikeCommentUseCase.java` — `Command(commentId, userId)`
- [ ] `GetPostLikersUseCase.java` — `Query(postId, page, size)`

### TASK-4.5 — Domain service: LikeService
- [ ] Create `backend/.../domain/service/LikeService.java`
  - Toggle-style: calls `hasLikedPost` before deciding to like or unlike
  - Increment/decrement `posts.like_count` and `comments.like_count` via `PostRepository` / `CommentRepository`

### TASK-4.6 — JPA entities & repositories
- [ ] `PostLikeJpaEntity.java` — `@Table(name = "post_likes")`, composite PK (`post_id`, `user_id`)
- [ ] `CommentLikeJpaEntity.java` — `@Table(name = "comment_likes")`, composite PK
- [ ] `PostLikeJpaRepository.java` — `existsByPostIdAndUserId`, `deleteByPostIdAndUserId`, `findByPostIdOrderByCreatedAtDesc`
- [ ] `CommentLikeJpaRepository.java` — same pattern for comments

### TASK-4.7 — Persistence adapter: LikePersistenceAdapter
- [ ] Create `LikePersistenceAdapter.java` — `implements LikeRepository`
  - Use `@Modifying @Query` to update `like_count` in the same transaction

### TASK-4.8 — REST controller: LikeController
- [ ] Create `LikeController.java`
  - `POST /api/v1/posts/{id}/like`
  - `DELETE /api/v1/posts/{id}/like`
  - `GET /api/v1/posts/{id}/likers` (paginated list of users)
  - `POST /api/v1/comments/{id}/like`
  - `DELETE /api/v1/comments/{id}/like`

### TASK-4.9 — Tests
- [ ] `LikeServiceTest.java` — like, unlike, double-like guard (Mockito)
- [ ] `LikePersistenceAdapterIT.java` — `@DataJpaTest`
- [ ] `LikeControllerTest.java` — MockMvc

---

## Backend — Comments

### TASK-4.10 — Domain model: Comment
- [ ] Create `backend/.../domain/model/Comment.java`
  - Fields: `id`, `postId`, `userId`, `parentId` (nullable for replies), `content`, `likeCount`, `status`, `createdAt`, `updatedAt`
  - Builder pattern (no Lombok)
  - Business methods: `withEdit(String newContent)`, `withSoftDelete()`

### TASK-4.11 — Domain exceptions
- [ ] Create `backend/.../domain/exception/CommentNotFoundException.java`
- [ ] Create `backend/.../domain/exception/UnauthorizedCommentAccessException.java`

### TASK-4.12 — Out-port: CommentRepository
- [ ] Create `backend/.../domain/port/out/CommentRepository.java`
  - `save(Comment)`, `findById(UUID)`, `deleteById(UUID)`
  - `findByPostId(UUID postId, Pageable)` — top-level comments
  - `findByParentId(UUID parentId, Pageable)` — replies

### TASK-4.13 — In-ports
- [ ] `AddCommentUseCase.java` — `Command(postId, userId, content, parentId?)`
- [ ] `EditCommentUseCase.java` — `Command(commentId, userId, newContent)`
- [ ] `DeleteCommentUseCase.java` — `Command(commentId, userId)`
- [ ] `GetCommentsUseCase.java` — `Query(postId, page, size)`
- [ ] `GetRepliesUseCase.java` — `Query(commentId, page, size)`

### TASK-4.14 — Domain service: CommentService
- [ ] Create `backend/.../domain/service/CommentService.java`
  - Implements all in-ports from TASK-4.13
  - Extract `@mention` usernames from `content` and create `Mention` records
  - Increment/decrement `posts.comment_count`

### TASK-4.15 — JPA entity & repository
- [ ] `CommentJpaEntity.java` — `@Table(name = "comments")`, self-referential `@ManyToOne` for `parentId`
- [ ] `CommentJpaRepository.java`
  - `findByPostIdAndParentIdIsNullAndStatusNot(UUID, CommentStatus, Pageable)`
  - `findByParentIdAndStatusNot(UUID, CommentStatus, Pageable)`

### TASK-4.16 — Persistence adapter: CommentPersistenceAdapter
- [ ] Create `CommentPersistenceAdapter.java` — `implements CommentRepository`

### TASK-4.17 — REST controller: CommentController
- [ ] Create `CommentController.java`
  - `GET /api/v1/posts/{id}/comments`
  - `POST /api/v1/posts/{id}/comments`
  - `GET /api/v1/comments/{id}/replies`
  - `PUT /api/v1/comments/{id}`
  - `DELETE /api/v1/comments/{id}`

### TASK-4.18 — Comment DTOs
- [ ] `AddCommentRequest.java` — `content` (`@NotBlank @Size(max=2200)`), `parentId?`
- [ ] `EditCommentRequest.java` — `content`
- [ ] `CommentResponse.java` — `id`, `postId`, `userId`, `username`, `avatarUrl`, `content`, `likeCount`, `replyCount`, `createdAt`, `isLikedByCurrentUser`, factory `from(...)`

### TASK-4.19 — Tests
- [ ] `CommentServiceTest.java` — add, reply, edit, delete (Mockito)
- [ ] `CommentPersistenceAdapterIT.java` — `@DataJpaTest`
- [ ] `CommentControllerTest.java` — MockMvc

---

## Backend — Saves

### TASK-4.20 — Domain model & out-port
- [ ] Create `backend/.../domain/model/SavedPost.java` — `id`, `postId`, `userId`, `savedAt`
- [ ] Create `backend/.../domain/port/out/SavedPostRepository.java`
  - `save(SavedPost)`, `delete(UUID postId, UUID userId)`, `existsByPostIdAndUserId(UUID, UUID)`, `findByUserId(UUID, Pageable)`

### TASK-4.21 — In-ports
- [ ] `SavePostUseCase.java` — `Command(postId, userId)`
- [ ] `UnsavePostUseCase.java` — `Command(postId, userId)`
- [ ] `GetSavedPostsUseCase.java` — `Query(userId, page, size)`

### TASK-4.22 — Domain service, JPA, persistence adapter, controller
- [ ] `SavedPostService.java` — implements save/unsave/list
- [ ] `SavedPostJpaEntity.java` — `@Table(name = "saved_posts")`, composite PK
- [ ] `SavedPostJpaRepository.java`
- [ ] `SavedPostPersistenceAdapter.java` — `implements SavedPostRepository`
- [ ] `SaveController.java`
  - `POST /api/v1/posts/{id}/save`
  - `DELETE /api/v1/posts/{id}/save`
  - `GET /api/v1/users/me/saved`

### TASK-4.23 — Tests
- [ ] `SavedPostServiceTest.java`, `SavedPostPersistenceAdapterIT.java`, `SaveControllerTest.java`

---

## Backend — Shares

### TASK-4.24 — Domain model & out-port
- [ ] Create `backend/.../domain/model/PostShare.java` — `id`, `postId`, `sharerId`, `recipientId?`, `shareType` (`LINK` / `DM`), `createdAt`
- [ ] Create `backend/.../domain/port/out/ShareRepository.java` — `save(PostShare)`, `findByPostId(UUID)`

### TASK-4.25 — In-port, service, JPA, adapter, controller
- [ ] `SharePostUseCase.java` — `Command(postId, sharerId, recipientId?, shareType)`
- [ ] `ShareService.java`
- [ ] `PostShareJpaEntity.java` — `@Table(name = "post_shares")`
- [ ] `SharePersistenceAdapter.java`
- [ ] `ShareController.java` — `POST /api/v1/posts/{id}/share`

---

## Frontend

### TASK-4.26 — TypeScript types
- [ ] Update `frontend/src/types/post.ts` — add `likedByCurrentUser`, `savedByCurrentUser`, `shareCount`
- [ ] Create `frontend/src/types/comment.ts` — `Comment`, `AddCommentPayload`, `EditCommentPayload`

### TASK-4.27 — API services
- [ ] Create `frontend/src/api/likesApi.ts` — `likePost`, `unlikePost`, `likeComment`, `unlikeComment`, `getPostLikers`
- [ ] Create `frontend/src/api/commentsApi.ts` — `getComments`, `addComment`, `getReplies`, `editComment`, `deleteComment`
- [ ] Create `frontend/src/api/savesApi.ts` — `savePost`, `unsavePost`, `getSavedPosts`
- [ ] Create `frontend/src/api/sharesApi.ts` — `sharePost`

### TASK-4.28 — Custom hooks
- [ ] `useLikePost.ts` — `useMutation` with optimistic like_count update
- [ ] `useComments.ts` — `useInfiniteQuery` for paginated comments
- [ ] `useAddComment.ts` — `useMutation`, invalidates comments query
- [ ] `useSavePost.ts` — `useMutation` with optimistic update

### TASK-4.29 — Like components
- [ ] Create `frontend/src/components/posts/LikeButton.tsx`
  - Animated heart icon (filled/outline toggle)
  - Optimistic update via `useLikePost`
  - Show like count next to icon
- [ ] Create `frontend/src/components/posts/LikersTooltip.tsx`
  - Shows "Liked by **user1**, **user2** and **N others**"
  - On click: opens `LikersDialog` with full list

### TASK-4.30 — Comment components
- [ ] Create `frontend/src/components/comments/CommentSection.tsx`
  - Paginated threaded comment list inside `PostDetailModal`
  - Each comment: avatar + username + content + like button + reply button + timestamp
- [ ] Create `frontend/src/components/comments/CommentInput.tsx`
  - `<TextField>` at bottom, submit on Enter or button click
  - `@mention` autocomplete dropdown
- [ ] Create `frontend/src/components/comments/CommentItem.tsx`
  - Shows comment with optional nested `CommentSection` for replies

### TASK-4.31 — Save button component
- [ ] Create `frontend/src/components/posts/SaveButton.tsx`
  - Bookmark icon, filled when saved
  - Optimistic update via `useSavePost`

### TASK-4.32 — Share menu component
- [ ] Create `frontend/src/components/posts/ShareMenu.tsx`
  - MUI `Menu` with options: "Copy Link" (clipboard), "Send as Message" (→ DM flow placeholder)

### TASK-4.33 — Saved posts page
- [ ] Create `frontend/src/pages/profile/SavedPostsPage.tsx`
  - Shows `PostGrid` with the current user's saved posts
  - Protected route

### TASK-4.34 — Integrate action components into PostCard
- [ ] Update `PostCard.tsx` to include `LikeButton`, `CommentTrigger`, `SaveButton`, `ShareMenu`
- [ ] Update `PostDetailModal.tsx` to embed `CommentSection` + `CommentInput`

### TASK-4.35 — Register routes
- [ ] Add route `/saved` → `SavedPostsPage` (protected)
