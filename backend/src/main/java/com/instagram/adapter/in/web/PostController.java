package com.instagram.adapter.in.web;

import com.instagram.adapter.in.web.dto.*;
import com.instagram.domain.model.Post;
import com.instagram.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Primary (driving) adapter — exposes post use cases as a REST API.
 *
 * <p>Depends only on input-port interfaces, never on {@code PostService} directly.</p>
 */
@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Posts", description = "CRUD operations for posts")
public class PostController {

    /** Seed user UUID — stands in for JWT auth (Phase 2). */
    private static final UUID DEFAULT_USER_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final CreatePostUseCase createPostUseCase;
    private final GetPostUseCase    getPostUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final ListPostsUseCase  listPostsUseCase;

    public PostController(
            CreatePostUseCase createPostUseCase,
            GetPostUseCase    getPostUseCase,
            UpdatePostUseCase updatePostUseCase,
            DeletePostUseCase deletePostUseCase,
            ListPostsUseCase  listPostsUseCase) {

        this.createPostUseCase = createPostUseCase;
        this.getPostUseCase    = getPostUseCase;
        this.updatePostUseCase = updatePostUseCase;
        this.deletePostUseCase = deletePostUseCase;
        this.listPostsUseCase  = listPostsUseCase;
    }

    // ── CREATE ────────────────────────────────────────────────────────────── //

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<PostResponse> createPost(
            @Parameter(description = "Caller's user ID (placeholder until JWT auth)")
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @Valid @RequestBody CreatePostRequest request) {

        UUID effectiveUserId = userId != null ? userId : DEFAULT_USER_ID;

        Post created = createPostUseCase.createPost(
                new CreatePostUseCase.CreatePostCommand(
                        effectiveUserId,
                        request.caption(),
                        request.location()));

        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(created));
    }

    // ── READ ONE ─────────────────────────────────────────────────────────── //

    @GetMapping("/{id}")
    @Operation(summary = "Get a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public PostResponse getPost(@PathVariable UUID id) {
        return PostResponse.from(getPostUseCase.getPost(id));
    }

    // ── READ LIST ─────────────────────────────────────────────────────────── //

    @GetMapping
    @Operation(summary = "List posts (all or by userId)")
    public PagedResponse<PostResponse> listPosts(
            @RequestParam(required = false) UUID userId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Post> posts = (userId != null)
                ? listPostsUseCase.listPostsByUser(userId, page, size)
                : listPostsUseCase.listAllPosts(page, size);

        List<PostResponse> content = posts.stream()
                .map(PostResponse::from)
                .toList();

        return PagedResponse.of(content, page, size);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────── //

    @PutMapping("/{id}")
    @Operation(summary = "Update caption or location of a post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public PostResponse updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequest request) {

        Post updated = updatePostUseCase.updatePost(
                new UpdatePostUseCase.UpdatePostCommand(
                        id, request.caption(), request.location()));

        return PostResponse.from(updated);
    }

    // ── DELETE ────────────────────────────────────────────────────────────── //

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft-delete a post")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post deleted"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        deletePostUseCase.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
