# Phase 2 — Posts & Media (Core Content)

> **Depends on:** Phase 0, Phase 1  
> **Blocks:** Phase 3, 4, 5  
> **BRD refs:** FR-005, FR-006, FR-007, FR-008  
> **DB tables:** `posts`, `post_media`, `post_media_edits`, `hashtags`, `post_hashtags`, `mentions`  
> **Branch prefix:** `feat/phase-2-`

---

## Backend

### TASK-2.1 — Domain model: Post aggregate
- [ ] Create `backend/.../domain/model/Post.java`
  - Fields (from schema): `id`, `userId`, `caption`, `location`, `likeCount`, `commentCount`, `shareCount`, `viewCount`, `status`, `createdAt`, `updatedAt`
  - Builder pattern (no Lombok)
  - Business methods: `withUpdatedCaption(String caption, String location)`, `withSoftDelete()`, `withIncrementedLike()`, `withDecrementedLike()`
- [ ] Create `backend/.../domain/model/PostMedia.java`
  - Fields: `id`, `postId`, `mediaType` (`IMAGE`/`VIDEO`/`REEL`), `mediaUrl`, `thumbnailUrl`, `width`, `height`, `duration`, `orderIndex`

### TASK-2.2 — Domain model: Hashtag
- [ ] Create `backend/.../domain/model/Hashtag.java`
  - Fields: `id`, `name`, `postCount`
  - Business method: `withIncrementedCount()`

### TASK-2.3 — Domain exceptions
- [ ] Create `backend/.../domain/exception/PostNotFoundException.java`
- [ ] Create `backend/.../domain/exception/UnauthorizedPostAccessException.java`
- [ ] Create `backend/.../domain/exception/MediaUploadException.java`

### TASK-2.4 — Out-ports
- [ ] Create `backend/.../domain/port/out/PostRepository.java`
  - `save(Post)`, `findById(UUID)`, `findByUserId(UUID, Pageable)`, `deleteById(UUID)`
- [ ] Create `backend/.../domain/port/out/PostMediaRepository.java`
  - `saveAll(List<PostMedia>)`, `findByPostId(UUID)`
- [ ] Create `backend/.../domain/port/out/HashtagRepository.java`
  - `findByName(String)`, `save(Hashtag)`, `findOrCreate(String name)`

### TASK-2.5 — In-ports (one file each)
- [ ] `CreatePostUseCase.java` — `Command(userId, caption, location, mediaItems: List<MediaItem>)`
- [ ] `UpdatePostUseCase.java` — `Command(postId, requesterId, caption, location)`
- [ ] `DeletePostUseCase.java` — `Command(postId, requesterId)`
- [ ] `GetPostUseCase.java` — `Query(postId, currentUserId)`
- [ ] `GetUserPostsUseCase.java` — `Query(targetUsername, currentUserId, cursor, limit)`
- [ ] `GenerateUploadUrlUseCase.java` — `Command(userId, filename, contentType)`, returns `UploadUrl(presignedUrl, mediaKey)`

### TASK-2.6 — Domain service: PostService
- [ ] Create `backend/.../domain/service/PostService.java`
  - Implements all in-ports from TASK-2.5
  - Hashtag extraction logic: parse `#word` from caption string
  - Mention extraction logic: parse `@username` from caption
  - Delegates media upload URL generation to `MediaStoragePort`
  - Delegates DB persistence to `PostRepository`, `PostMediaRepository`, `HashtagRepository`

### TASK-2.7 — JPA entities
- [ ] Create `PostJpaEntity.java` — `@Entity @Table(name = "posts")`, extends `BaseJpaEntity`
- [ ] Create `PostMediaJpaEntity.java` — `@Entity @Table(name = "post_media")`
- [ ] Create `HashtagJpaEntity.java` — `@Entity @Table(name = "hashtags")`
- [ ] Create `PostHashtagJpaEntity.java` — `@Entity @Table(name = "post_hashtags")` (join table with composite key)
- [ ] Create `MentionJpaEntity.java` — `@Entity @Table(name = "mentions")`

### TASK-2.8 — JPA repositories
- [ ] `PostJpaRepository.java` — `findByUserIdAndStatusNot(UUID, PostStatus, Pageable)`, `findByIdAndStatusNot(UUID, PostStatus)`
- [ ] `PostMediaJpaRepository.java` — `findByPostIdOrderByOrderIndexAsc(UUID)`
- [ ] `HashtagJpaRepository.java` — `findByName(String)`, `findTopByOrderByPostCountDesc(Pageable)` (for explore)
- [ ] `MentionJpaRepository.java` — `findByPostId(UUID)`, `findByMentionedUserId(UUID, Pageable)`

### TASK-2.9 — Persistence adapters
- [ ] Create `PostPersistenceAdapter.java` — `implements PostRepository` with private `toEntity` / `toDomain`
- [ ] Create `PostMediaPersistenceAdapter.java` — `implements PostMediaRepository`
- [ ] Create `HashtagPersistenceAdapter.java` — `implements HashtagRepository`

### TASK-2.10 — MinIO pre-signed URL adapter
- [ ] Update `MinioStorageAdapter.java` (from Phase 1 TASK-1.15) to add:
  - `generatePresignedPutUrl(String key, Duration expiry) → String`
- [ ] Ensure `MediaStoragePort` interface includes `generatePresignedPutUrl`

### TASK-2.11 — REST controllers & DTOs
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

### TASK-2.12 — Register exception mappings
- [ ] Map `PostNotFoundException` → `404`
- [ ] Map `UnauthorizedPostAccessException` → `403`
- [ ] Map `MediaUploadException` → `500`

### TASK-2.13 — Tests
- [ ] `PostServiceTest.java` — create, update, delete, hashtag extraction (unit, Mockito)
- [ ] `PostPersistenceAdapterIT.java` — `@DataJpaTest`, test save / find / soft-delete
- [ ] `PostControllerTest.java` — `@SpringBootTest` + MockMvc, test all CRUD endpoints with auth

---

## Frontend

### TASK-2.14 — TypeScript types
- [ ] Create `frontend/src/types/post.ts`
  - `Post`, `PostMedia`, `PostStatus`, `CreatePostPayload`, `UpdatePostPayload`, `MediaItem`, `UploadUrlResponse`

### TASK-2.15 — API services
- [ ] Create `frontend/src/api/postsApi.ts`
  - `createPost`, `getPostById`, `updatePost`, `deletePost`, `getUserPosts(username, cursor)`
- [ ] Create `frontend/src/api/mediaApi.ts`
  - `getUploadUrl(filename, contentType)` → presigned URL
  - `uploadToS3(presignedUrl, file)` → direct PUT to MinIO

### TASK-2.16 — Custom hooks
- [ ] Create `frontend/src/hooks/usePosts.ts` — `useQuery` for `getUserPosts` with cursor pagination
- [ ] Create `frontend/src/hooks/usePost.ts` — `useQuery` for single post
- [ ] Create `frontend/src/hooks/useCreatePost.ts` — `useMutation` wrapping upload + create flow
- [ ] Create `frontend/src/hooks/useDeletePost.ts` — `useMutation` with cache invalidation

### TASK-2.17 — Media upload component
- [ ] Create `frontend/src/components/posts/MediaPicker.tsx`
  - Drag & drop + file input button
  - Preview thumbnails for picked images/videos
  - Max 10 files limit
- [ ] Create `frontend/src/components/posts/MediaCropEditor.tsx`
  - Integrate `react-easy-crop` for image cropping
  - Output cropped image as `Blob`

### TASK-2.18 — Caption editor component
- [ ] Create `frontend/src/components/posts/CaptionEditor.tsx`
  - `<textarea>` with live `#hashtag` highlighting (bold blue) using regex + span injection
  - `@mention` detection with basic user autocomplete dropdown (calls `/api/v1/search?q=&type=users`)
  - Character counter (max 2200)

### TASK-2.19 — Post creation page/modal
- [ ] Create `frontend/src/components/posts/CreatePostModal.tsx`
  - Step 1: `MediaPicker` → Step 2: `MediaCropEditor` → Step 3: `CaptionEditor` + location text input
  - Progress indicator (MUI Stepper)
  - Submit calls `useCreatePost` hook

### TASK-2.20 — Post display components
- [ ] Create `frontend/src/components/posts/PostCard.tsx`
  - Displays: author avatar + username, carousel of post media (MUI Carousel or Swiper), caption, hashtags, like/comment/save/share action row, timestamp
  - Truncated caption with "more" toggle at 3 lines
- [ ] Create `frontend/src/components/posts/PostDetailModal.tsx`
  - Full-screen dialog: left = media, right = comments panel
- [ ] Create `frontend/src/components/posts/PostGrid.tsx`
  - 3-column `ImageList` with hover overlay (like/comment counts)
  - Clicking a cell opens `PostDetailModal`

### TASK-2.21 — Pages
- [ ] Create `frontend/src/pages/posts/PostPage.tsx` — standalone post page at `/p/:postId`

### TASK-2.22 — Register routes in App.tsx
- [ ] Add route `/p/:postId` → `PostPage`
