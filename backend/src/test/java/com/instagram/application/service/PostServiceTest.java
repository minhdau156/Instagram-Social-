package com.instagram.application.service;

import com.instagram.domain.exception.PostNotFoundException;
import com.instagram.domain.model.Post;
import com.instagram.domain.model.PostStatus;
import com.instagram.domain.port.in.CreatePostUseCase;
import com.instagram.domain.port.in.UpdatePostUseCase;
import com.instagram.domain.port.out.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService — Unit Tests")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private UUID userId;
    private UUID postId;
    private Post samplePost;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        postId = UUID.randomUUID();
        samplePost = Post.builder()
                .id(postId)
                .userId(userId)
                .caption("Hello world!")
                .location("Hanoi, Vietnam")
                .status(PostStatus.PUBLISHED)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    // ── CREATE ─────────────────────────────────────────────────────────────── //

    @Test
    @DisplayName("createPost() saves and returns the new post")
    void createPost_shouldSaveAndReturn() {
        when(postRepository.save(any(Post.class))).thenReturn(samplePost);

        Post result = postService.createPost(
                new CreatePostUseCase.CreatePostCommand(userId, "Hello world!", "Hanoi, Vietnam"));

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCaption()).isEqualTo("Hello world!");
        verify(postRepository, times(1)).save(any(Post.class));
    }

    // ── GET ────────────────────────────────────────────────────────────────── //

    @Test
    @DisplayName("getPost() returns post when found")
    void getPost_shouldReturnPost_whenFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(samplePost));

        Post result = postService.getPost(postId);

        assertThat(result.getId()).isEqualTo(postId);
    }

    @Test
    @DisplayName("getPost() throws PostNotFoundException when not found")
    void getPost_shouldThrow_whenNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────── //

    @Test
    @DisplayName("updatePost() applies caption changes and saves")
    void updatePost_shouldApplyChangesAndSave() {
        Post updatedPost = samplePost.withUpdateCaptionAndLocation("New caption", null);
        when(postRepository.findById(postId)).thenReturn(Optional.of(samplePost));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        Post result = postService.updatePost(
                new UpdatePostUseCase.UpdatePostCommand(postId, "New caption", null));

        assertThat(result.getCaption()).isEqualTo("New caption");
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("updatePost() throws PostNotFoundException when post missing")
    void updatePost_shouldThrow_whenNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost(
                new UpdatePostUseCase.UpdatePostCommand(postId, "New", null)))
                .isInstanceOf(PostNotFoundException.class);
    }

    // ── DELETE ─────────────────────────────────────────────────────────────── //

    @Test
    @DisplayName("deletePost() soft-deletes the post")
    void deletePost_shouldSoftDelete() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(samplePost));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        postService.deletePost(postId);

        verify(postRepository).save(argThat(p -> p.getDeletedAt() != null && p.getStatus() == PostStatus.DELETED));
    }

    @Test
    @DisplayName("deletePost() throws PostNotFoundException when post missing")
    void deletePost_shouldThrow_whenNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePost(postId))
                .isInstanceOf(PostNotFoundException.class);
    }

    // ── LIST ───────────────────────────────────────────────────────────────── //

    @Test
    @DisplayName("listAllPosts() delegates to repository")
    void listAllPosts_shouldDelegateToRepository() {
        when(postRepository.findAll(0, 10)).thenReturn(List.of(samplePost));

        List<Post> result = postService.listAllPosts(0, 10);

        assertThat(result).hasSize(1);
        verify(postRepository).findAll(0, 10);
    }

    @Test
    @DisplayName("listPostsByUser() filters by userId")
    void listPostsByUser_shouldFilterByUserId() {
        when(postRepository.findByUserId(userId, 0, 10)).thenReturn(List.of(samplePost));

        List<Post> result = postService.listPostsByUser(userId, 0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
    }
}
