package com.classmate.post_service.service.impl;

import com.classmate.post_service.client.ICommentClient;
import com.classmate.post_service.dto.*;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.InvalidPostException;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.mapper.IPostMapper;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import com.classmate.post_service.service.IPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the PostService interface for managing posts.
 */
@Service
public class PostServiceImpl implements IPostService {

    private final IPostRepository postRepository;
    private final IPostMapper postMapper;
    private final ICommentClient commentClient;

    private final PostPublisher postPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    /**
     * Constructs a new PostServiceImpl with the specified repository, mapper, and comment client.
     *
     * @param postRepository the post repository
     * @param postMapper the post mapper
     * @param commentClient the comment client
     * @param postPublisher RabbitMQ's publisher
     */
    public PostServiceImpl(IPostRepository postRepository, IPostMapper postMapper, ICommentClient commentClient, PostPublisher postPublisher) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.commentClient = commentClient;
        this.postPublisher = postPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APIResponseDTO getPostById(Long id) {
        LOGGER.info("Getting post by id...");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        APIResponseDTO apiResponseDTO = postMapper.convertToAPIResponseDTO(post);
        List<CommentDTO> commentDTOS = commentClient.getCommentsByPostId(id, 0, 10);
        apiResponseDTO.setCommentDTOS(commentDTOS);
        return apiResponseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostDTO> getPostsByName(String name, int page, int size) {
        LOGGER.info("Getting posts by name...");
        Page<Post> postsPage = postRepository.findByTitleContainingIgnoreCaseOrderByCreationDateDesc(name, PageRequest.of(page, size));
        return postsPage.getContent().stream().map(postMapper::convertToPostDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostDTO> getPostsByForumId(Long forumId, int page, int size) {
        LOGGER.info("Getting posts by forum id...");
        Pageable pageRequest = PageRequest.of(page, size);
        return postRepository.findByForumId(forumId, pageRequest)
                .map(postMapper::convertToPostDTO)
                .getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostDTO savePost(PostDTO postDTO) {
        validatePost(postDTO.getTitle(), postDTO.getBody());
        LOGGER.info("Saving post...");
        Post post = postMapper.convertToPost(postDTO);
        post.setId(null);
        Post savedPost = postRepository.save(post);
        return postMapper.convertToPostDTO(savedPost);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePost(Long id, PostUpdateDTO postUpdateDTO) {
        validatePost(postUpdateDTO.getTitle(), postUpdateDTO.getBody());
        LOGGER.info("Updating post by id...");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        post.setTitle(postUpdateDTO.getTitle());
        post.setBody(postUpdateDTO.getBody());
        postRepository.save(post);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePost(Long id, Long userId) {
        LOGGER.info("Deleting post by id...");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));

        if (!post.getAuthorId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this post");
        }
        postRepository.delete(post);

        PostDeletionDTO postDeletionDTO = PostDeletionDTO.builder()
                .postId(id)
                .build();
        postPublisher.publishPostDeletion(postDeletionDTO);
    }

    /**
     * Validates the post data.
     *
     * @param title the title of the post to validate
     * @param body the body of the post to validate
     */
    private void validatePost(String title, String body) {
        if (title == null || title.isEmpty()) {
            throw new InvalidPostException("Post title cannot be empty");
        }
        if (body == null || body.isEmpty()) {
            throw new InvalidPostException("Post body cannot be empty");
        }
        if (body.length() > 5000) {
            throw new InvalidPostException("Post body cannot exceed 5000 characters");
        }
        if (title.length() > 300) {
            throw new InvalidPostException("Post title cannot exceed 300 characters");
        }
    }
}
