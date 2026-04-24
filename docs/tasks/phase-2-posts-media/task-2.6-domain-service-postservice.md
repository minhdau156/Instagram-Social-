# TASK-2.6 — Domain service: PostService

## 📝 Overview

The goal of this task is to implement the **Domain service: PostService** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Service Logic
- Implement the defined Use Case interfaces.
- Inject Out-Ports (Repositories, StorageAdapters) via constructor.
- Handle cross-cutting concerns like Hashtag extraction and Mention detection internally or delegate to domain helpers.
- Ensure transactions are NOT managed here (they are managed in the persistence adapter or controller boundary depending on your architecture).

## 📂 File Locations

```text
backend/src/main/java/com/instagram/domain/service/PostService.java
```

## 🧪 Testing Strategy

- **Unit Tests:** Use JUnit 5 and Mockito to test business logic in isolation.
- **Integration Tests:** Use `@DataJpaTest` for repositories and `@SpringBootTest` with `MockMvc` for controllers.

## 💡 Example

```java
// PostService.java
public class PostService implements CreatePostUseCase, UpdatePostUseCase, DeletePostUseCase {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final HashtagRepository hashtagRepository;
    private final MediaStoragePort mediaStoragePort;

    // Inject via constructor (no Spring annotations here)
    public PostService(PostRepository postRepository, PostMediaRepository postMediaRepository,
                       HashtagRepository hashtagRepository, MediaStoragePort mediaStoragePort) {
        this.postRepository = postRepository;
        this.postMediaRepository = postMediaRepository;
        this.hashtagRepository = hashtagRepository;
        this.mediaStoragePort = mediaStoragePort;
    }

    @Override
    public Post createPost(CreatePostCommand command) {
        Post post = Post.builder()
            .id(UUID.randomUUID())
            .userId(command.userId())
            .caption(command.caption())
            .location(command.location())
            .status(PostStatus.PUBLISHED)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        Post saved = postRepository.save(post);
        // Extract and link hashtags from caption
        extractHashtags(command.caption()).forEach(tag -> {
            Hashtag hashtag = hashtagRepository.findOrCreate(tag);
            hashtagRepository.save(hashtag.withIncrementedCount());
        });
        return saved;
    }

    private List<String> extractHashtags(String caption) {
        if (caption == null) return List.of();
        return Pattern.compile("#(\\w+)").matcher(caption).results()
            .map(r -> r.group(1).toLowerCase())
            .distinct().toList();
    }
}
```

## ✅ Checklist

- [x] Create `backend/.../domain/service/PostService.java`
  - Implements all in-ports from TASK-2.5
  - Hashtag extraction logic: parse `#word` from caption string
  - Mention extraction logic: parse `@username` from caption
  - Delegates media upload URL generation to `MediaStoragePort`
  - Delegates DB persistence to `PostRepository`, `PostMediaRepository`, `HashtagRepository`
