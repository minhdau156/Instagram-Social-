package com.instagram.application.service;

import com.instagram.domain.exception.PostNotFoundException;
import com.instagram.domain.model.Post;
import com.instagram.domain.model.PostStatus;
import com.instagram.domain.port.in.*;
import com.instagram.domain.port.out.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Application service orchestrating all post-related use cases.
 *
 * <p>
 * Implements every input port for posts. Depends only on the
 * {@link PostRepository}
 * output port — zero JPA or HTTP knowledge here.
 * </p>
 */
@Service
@Transactional(readOnly = true)
public class PostService implements
        CreatePostUseCase,
        GetPostUseCase,
        UpdatePostUseCase,
        DeletePostUseCase,
        ListPostsUseCase {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // ── CreatePostUseCase ─────────────────────────────────────────────────── //

    @Override
    @Transactional
    public Post createPost(CreatePostCommand command) {
        Post newPost = Post.builder()
                .userId(command.userId())
                .caption(command.caption())
                .location(command.location())
                .status(PostStatus.PUBLISHED)
                .viewCount(0L)
                .likeCount(0)
                .commentCount(0)
                .saveCount(0)
                .shareCount(0)
                .build();

        return postRepository.save(newPost);
    }

    // ── GetPostUseCase ────────────────────────────────────────────────────── //

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    // ── UpdatePostUseCase ─────────────────────────────────────────────────── //

    @Override
    @Transactional
    public Post updatePost(UpdatePostCommand command) {
        Post existing = postRepository.findById(command.id())
                .orElseThrow(() -> new PostNotFoundException(command.id()));

        Post updated = existing.withUpdateCaptionAndLocation(command.caption(), command.location());
        return postRepository.save(updated);
    }

    // ── DeletePostUseCase ─────────────────────────────────────────────────── //

    @Override
    @Transactional
    public void deletePost(UUID id) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        Post softDeleted = existing.withSoftDelete();
        postRepository.save(softDeleted);
    }

    // ── ListPostsUseCase ──────────────────────────────────────────────────── //

    @Override
    public List<Post> listAllPosts(int page, int size) {
        return postRepository.findAll(page, size);
    }

    @Override
    public List<Post> listPostsByUser(UUID userId, int page, int size) {
        return postRepository.findByUserId(userId, page, size);
    }
}
