package com.classmate.post_service.service.impl;

import com.classmate.post_service.client.ICommentClient;
import com.classmate.post_service.client.IFileServiceClient;
import com.classmate.post_service.dto.*;
import com.classmate.post_service.dto.filedtos.FileDeletionDTO;
import com.classmate.post_service.dto.filedtos.PostFileDeletionDTO;
import com.classmate.post_service.entity.Attachment;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the PostService interface for managing posts.
 */
@Service
public class PostServiceImpl implements IPostService {

    private final IPostRepository postRepository;
    private final IPostMapper postMapper;
    private final ICommentClient commentClient;
    private final IFileServiceClient fileServiceClient;
    private final PostPublisher postPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    public PostServiceImpl(IPostRepository postRepository, IPostMapper postMapper, ICommentClient commentClient, IFileServiceClient fileServiceClient, PostPublisher postPublisher) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.commentClient = commentClient;
        this.fileServiceClient = fileServiceClient;
        this.postPublisher = postPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APIResponseDTO getPostById(Long id, Long userId) {
        LOGGER.info("Getting post by id...");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        APIResponseDTO apiResponseDTO = postMapper.convertToAPIResponseDTO(post);

        apiResponseDTO.setLikedByUser(post.getUpvotesByUserId().contains(userId));
        apiResponseDTO.setDislikedByUser(post.getDownvotesByUserId().contains(userId));
        apiResponseDTO.setValoration(post.getValoration());
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
    public List<PostResponseDTO> getPostsByForumId(Long forumId, Long userId, int page, int size) {
        LOGGER.info("Getting posts by forum id...");
        Pageable pageRequest = PageRequest.of(page, size);
        return postRepository.findByForumId(forumId, pageRequest)
                .map(post -> getPostResponseDTO(post, userId))
                .getContent();
    }

    public IsPostAuthorDTO isPostAuthor(Long postId, Long authorId){
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %d not found", postId)));
        IsPostAuthorDTO isPostAuthorDTO = IsPostAuthorDTO.builder().isAuthor(false).build();
        if(existingPost.getAuthorId().equals(authorId)){
            isPostAuthorDTO.setAuthor(true);
        }
        return isPostAuthorDTO;
    }

    public List<PostResponseDTO> getPostsBySubscribedForums(RequestByForumsDTO requestByForumsDTO, Long userId, int page, int size){
        List<Long> forumIds = requestByForumsDTO.getForumIds();
        LOGGER.info("Getting posts of forums: " + forumIds + " page: " + page + ", size: " + size);

        if(forumIds.isEmpty()){
            return new ArrayList<>();
        }

        Pageable pageRequest = PageRequest.of(page, size);
        return postRepository.findByForumIdInOrderByCreationDateDesc(forumIds, pageRequest)
                .map(post -> getPostResponseDTO(post, userId))
                .getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostResponseDTO savePost(PostRequestDTO postRequestDTO) {
        LOGGER.info("Saving post...");
        validatePost(postRequestDTO.getTitle(), postRequestDTO.getBody());
        if (postRequestDTO.getFiles() != null && !postRequestDTO.getFiles().isEmpty()) {
            validateAttachments(postRequestDTO.getFiles());
        }

        List<Attachment> attachments = uploadFiles(postRequestDTO.getFiles());

        Post post = postMapper.mapToPost(postRequestDTO);
        post.setCreationDate(LocalDateTime.now());
        post.setAttachments(attachments);
        post.addUpvote(postRequestDTO.getAuthorId());
        Post savedPost = postRepository.save(post);
        PostResponseDTO postResponseDTO = postMapper.convertToPostResponseDTO(savedPost);
        postResponseDTO.setLikedByUser(true);
        postResponseDTO.setDislikedByUser(false);
        postResponseDTO.setValoration(1);
        return postResponseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePost(Long id, PostUpdateDTO postUpdateDTO) {
        LOGGER.info("Updating post by id...");
        validatePost(postUpdateDTO.getTitle(), postUpdateDTO.getBody());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));

        post.setTitle(postUpdateDTO.getTitle());
        post.setBody(postUpdateDTO.getBody());

        if (postUpdateDTO.getFileIdsToRemove() != null && !postUpdateDTO.getFileIdsToRemove().isEmpty()) {
            removeAttachments(post, postUpdateDTO.getFileIdsToRemove());
        }

        if (postUpdateDTO.getFilesToAdd() != null && !postUpdateDTO.getFilesToAdd().isEmpty()) {
            validateAttachmentsForUpdate(post, postUpdateDTO.getFilesToAdd());
            addAttachments(post, postUpdateDTO.getFilesToAdd());
        }

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

        List<Long> attachmentIds = post.getAttachments().stream()
                .map(Attachment :: getId)
                .toList();

        // Publish file deletion event for all attachments
        PostFileDeletionDTO postFileDeletionDTO = PostFileDeletionDTO.builder()
                .attachmentIdsToDelete(attachmentIds)
                .build();
        postPublisher.publishPostAllFileDeleteEvent(postFileDeletionDTO);

        // Publish post deletion event
        PostDeletionDTO postDeletionDTO = PostDeletionDTO.builder()
                .postId(id)
                .build();
        postPublisher.publishPostDeletion(postDeletionDTO);

        // Delete the post
        postRepository.delete(post);
    }

    public void addAttachments(Post post, List<MultipartFile> filesToAdd) {
        for (MultipartFile file : filesToAdd) {
            Long fileId = Objects.requireNonNull(fileServiceClient.uploadFile(file).getBody()).getFileId();
            Attachment attachment = Attachment.builder()
                    .id(fileId)
                    .originalFilename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();
            post.addAttachment(attachment);
        }
    }

    public void removeAttachments(Post post, List<Long> fileIdsToRemove) {
        List<Long> validFileIdsToRemove = validateFileIdsToRemove(post, fileIdsToRemove);

        // Remove the attachments
        post.removeAttachments(validFileIdsToRemove);

        // Publish the events after updating the state
        for (Long fileId : validFileIdsToRemove) {
            FileDeletionDTO event = new FileDeletionDTO(fileId);
            postPublisher.publishPostFileDeleteEvent(event);
        }
    }

    private List<Attachment> uploadFiles(List<MultipartFile> files) {
        List<Attachment> attachments = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                Long fileId = Objects.requireNonNull(fileServiceClient.uploadFile(file).getBody()).getFileId();
                attachments.add(Attachment.builder()
                        .id(fileId)
                        .contentType(file.getContentType())
                        .originalFilename(file.getOriginalFilename())
                        .size(file.getSize())
                        .build());
            }
        }
        return attachments;
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

    private void validateAttachments(List<MultipartFile> files) {
        if (files.size() > 5) {
            throw new IllegalArgumentException("No more than 5 attachments allowed.");
        }
        for (MultipartFile file : files) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("File size should not exceed 10MB.");
            }
        }
    }

    private void validateAttachmentsForUpdate(Post post, List<MultipartFile> filesToAdd) {
        int totalAttachments = post.getAttachments().size() + filesToAdd.size();
        if (totalAttachments > 5) {
            throw new IllegalArgumentException("No more than 5 attachments allowed.");
        }
        for (MultipartFile file : filesToAdd) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("File size should not exceed 10MB.");
            }
        }
    }

    public List<Long> validateFileIdsToRemove(Post post, List<Long> fileIdsToRemove) {
        return post.getAttachments().stream()
                .filter(attachment -> fileIdsToRemove.contains(attachment.getId()))
                .map(Attachment :: getId)
                .toList();
    }

    public PostResponseDTO getPostResponseDTO(Post post, Long userId){
        PostResponseDTO postResponseDTO = postMapper.convertToPostResponseDTO(post);
        postResponseDTO.setLikedByUser(post.getUpvotesByUserId().contains(userId));
        postResponseDTO.setDislikedByUser(post.getDownvotesByUserId().contains(userId));
        postResponseDTO.setValoration(post.getValoration());
        return postResponseDTO;
    }
}
