package com.instagram.adapter.in.web;

import com.instagram.adapter.in.web.dto.*;
import com.instagram.domain.model.Post;
import com.instagram.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Posts", description = "CRUD operations for posts")
public class PostController {

        private static final UUID DEFAULT_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

        private final CreatePostUseCase createPostUseCase;
        private final GetPostUseCase getPostUseCase;
        private final UpdatePostUseCase updatePostUseCase;
        private final DeletePostUseCase deletePostUseCase;
        private final GetUserPostsUseCase getUserPostsUseCase;

        public PostController(
                        CreatePostUseCase createPostUseCase,
                        GetPostUseCase getPostUseCase,
                        UpdatePostUseCase updatePostUseCase,
                        DeletePostUseCase deletePostUseCase,
                        GetUserPostsUseCase getUserPostsUseCase) {

                this.createPostUseCase = createPostUseCase;
                this.getPostUseCase = getPostUseCase;
                this.updatePostUseCase = updatePostUseCase;
                this.deletePostUseCase = deletePostUseCase;
                this.getUserPostsUseCase = getUserPostsUseCase;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create a new post")
        public ResponseEntity<ApiResponse<PostResponse>> createPost(
                        @Parameter(description = "Caller's user ID (placeholder until JWT auth)") @RequestHeader(value = "X-User-Id", required = false) UUID userId,
                        @Valid @RequestBody CreatePostRequest request) {

                UUID effectiveUserId = userId != null ? userId : DEFAULT_USER_ID;

                // Temporary empty media items to make it compile until TASK-2.11 DTO updates
                Post created = createPostUseCase.createPost(
                                new CreatePostUseCase.Command(
                                                effectiveUserId,
                                                request.caption(),
                                                request.location(),
                                                Collections.emptyList()));

                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(PostResponse.from(created)));
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get a post by ID")
        public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.ok(PostResponse.from(getPostUseCase.getPost(new GetPostUseCase.Query(id, DEFAULT_USER_ID)))));
        }

        @GetMapping
        @Operation(summary = "List posts (all or by userId)")
        public ResponseEntity<ApiResponse<PagedResponse<PostResponse>>> listPosts(
                        @RequestParam(required = false) UUID userId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {

                List<Post> posts;
                if (userId != null) {
                        posts = getUserPostsUseCase.getUserPosts(new GetUserPostsUseCase.Query(userId, DEFAULT_USER_ID, String.valueOf(page), size)).getContent();
                } else {
                        posts = Collections.emptyList(); // Just for compiling
                }

                List<PostResponse> content = posts.stream()
                                .map(PostResponse::from)
                                .toList();

                return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(PagedResponse.of(content, page, size)));
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update caption or location of a post")
        public ResponseEntity<ApiResponse<PostResponse>> updatePost(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdatePostRequest request) {

                Post updated = updatePostUseCase.updatePost(
                                new UpdatePostUseCase.Command(
                                                id, DEFAULT_USER_ID, request.caption(), request.location()));

                return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(PostResponse.from(updated)));
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(summary = "Soft-delete a post")
        public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable UUID id) {
                deletePostUseCase.deletePost(new DeletePostUseCase.Command(id, DEFAULT_USER_ID));
                return ResponseEntity.noContent().build();
        }
}
