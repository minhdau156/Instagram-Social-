# TASK-2.13 — Tests

## 📝 Overview

The goal of this task is to implement the **Tests** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

Follow established project patterns to complete this task successfully.

## 📂 File Locations

```text
backend/src/test/java/com/instagram/PostServiceTest.java
backend/src/main/java/com/instagram/infrastructure/persistence/adapter/PostPersistenceAdapterIT.java
backend/src/main/java/com/instagram/infrastructure/web/controller/PostControllerTest.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// PostServiceTest.java — unit test with Mockito
class PostServiceTest {
    @Mock PostRepository postRepository;
    @Mock HashtagRepository hashtagRepository;
    @InjectMocks PostService postService;

    @Test
    void createPost_shouldSavePostAndExtractHashtags() {
        UUID userId = UUID.randomUUID();
        var command = new CreatePostUseCase.CreatePostCommand(
            userId, "Hello #world", null, List.of(mockMediaItem()));
        when(postRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(hashtagRepository.findOrCreate("world")).thenReturn(Hashtag.builder().name("world").postCount(0).build());

        Post result = postService.createPost(command);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCaption()).isEqualTo("Hello #world");
        verify(hashtagRepository).findOrCreate("world");
    }
}

// PostControllerTest.java — integration test with MockMvc
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user-uuid-here")
    void createPost_shouldReturn201() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{ "caption": "Test post", "mediaItems": [...] }"""))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.caption").value("Test post"));
    }
}
```

## ✅ Checklist

- [ ] `PostServiceTest.java` — create, update, delete, hashtag extraction (unit, Mockito)
- [ ] `PostPersistenceAdapterIT.java` — `@DataJpaTest`, test save / find / soft-delete
- [ ] `PostControllerTest.java` — `@SpringBootTest` + MockMvc, test all CRUD endpoints with auth

---

## Frontend
