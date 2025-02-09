package com.classmate.post_service.service.impl;

import com.classmate.post_service.client.IAuthClient;
import com.classmate.post_service.client.ICommentClient;
import com.classmate.post_service.client.IFileServiceClient;
import com.classmate.post_service.dto.*;
import com.classmate.post_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.post_service.dto.filedtos.FileDeletionDTO;
import com.classmate.post_service.dto.filedtos.PostFileDeletionDTO;
import com.classmate.post_service.dto.user.UserDTO;
import com.classmate.post_service.entity.Attachment;
import com.classmate.post_service.entity.DeleteRequest;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.entity.User;
import com.classmate.post_service.entity.enums.Role;
import com.classmate.post_service.exception.InvalidPostException;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.exception.UserNotFoundException;
import com.classmate.post_service.mapper.DeleteRequestMapper;
import com.classmate.post_service.mapper.IPostMapper;
import com.classmate.post_service.mapper.IUserMapper;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import com.classmate.post_service.repository.IUserRepository;
import com.classmate.post_service.service.IJWTService;
import com.classmate.post_service.service.IPostService;
import com.classmate.post_service.service.IPostValorationService;
import jakarta.transaction.Transactional;
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
    private final IUserMapper userMapper;
    private final ICommentClient commentClient;
    private final IFileServiceClient fileServiceClient;
    private final IAuthClient authClient;
    private final PostPublisher postPublisher;
    private final IUserRepository userRepository;
    private final IPostValorationService valorationService;
    private final IJWTService jwtService;
    private final DeleteRequestMapper deleteRequestMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    public PostServiceImpl(IPostRepository postRepository, IPostMapper postMapper, IUserMapper userMapper, ICommentClient commentClient, IFileServiceClient fileServiceClient, IAuthClient authClient, PostPublisher postPublisher, IUserRepository userRepository, IPostValorationService valorationService, IJWTService jwtService, DeleteRequestMapper deleteRequestMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.commentClient = commentClient;
        this.fileServiceClient = fileServiceClient;
        this.authClient = authClient;
        this.postPublisher = postPublisher;
        this.userRepository = userRepository;
        this.valorationService = valorationService;
        this.jwtService = jwtService;
        this.deleteRequestMapper = deleteRequestMapper;
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

        UserDTO author = userMapper.mapUserToUserDTO(post.getAuthor());
        apiResponseDTO.setAuthor(author);

        apiResponseDTO.setLikedByUser(post.getUpvotesByUserId().contains(userId));
        apiResponseDTO.setDislikedByUser(post.getDownvotesByUserId().contains(userId));
        apiResponseDTO.setValoration(post.getValoration());
        apiResponseDTO.setHasBeenEdited(post.getHasBeenEdited());
        List<CommentDTO> commentDTOS = commentClient.getCommentsByPostId(id, userId, 0, 10);
        apiResponseDTO.setCommentDTOS(commentDTOS);
        apiResponseDTO.setReportedByUser(post.getDeleteRequests()
                                                .stream()
                                                .anyMatch((DeleteRequest deleteRequest) -> deleteRequest.getReporterId().equals(userId)));
        return apiResponseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostResponseDTO> getPostsByName(String name, Long userId, int page, int size) {
        LOGGER.info("Getting posts by name...");
        Page<Post> postsPage = postRepository.findByTitleContainingIgnoreCaseOrderByCreationDateDesc(name, PageRequest.of(page, size));
        return postsPage.getContent().stream().map(post -> getPostResponseDTO(post, userId)).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostsByNameAndForumId(String name, Long forumId, Long userId, int page, int size) {
        LOGGER.info("Getting posts by name and forumId...");
        Page<Post> postsPage = postRepository.findByTitleContainingIgnoreCaseAndForumIdOrderByCreationDateDesc(name, forumId,PageRequest.of(page, size));
        return postsPage.getContent().stream().map(post -> getPostResponseDTO(post, userId)).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostsByAuthorId(Long authorId, Long userId,int page, int size) {
        Page<Post> posts = postRepository.findAllByAuthor_UserId(authorId, PageRequest.of(page, size));
        return posts.getContent().stream()
                .map((Post p) -> getPostResponseDTO(p, userId))
                .collect(Collectors.toList());
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
        if(existingPost.getAuthor().getUserId().equals(authorId)){
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

    @Override
    @Transactional
    public void reportPost(Long postId, DeleteRequestDTO deleteRequestDTO) {
        Post reportedPost = postRepository.findById(postId)
                                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: %d not found.", postId)));
        DeleteRequest deleteRequest = deleteRequestMapper.mapToEntity(deleteRequestDTO);
        reportedPost.addDeleteRequest(deleteRequest);
        postRepository.save(reportedPost);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostResponseDTO savePost(PostSaveDTO postSaveDTO) {
        LOGGER.info("Saving post...");
        validatePost(postSaveDTO.getTitle(), postSaveDTO.getBody());
        if (postSaveDTO.getFiles() != null && !postSaveDTO.getFiles().isEmpty()) {
            validateAttachments(postSaveDTO.getFiles());
        }

        List<Attachment> attachments = uploadFiles(postSaveDTO.getFiles());

        Post post = postMapper.mapToPost(postSaveDTO);

        User author = userRepository.findById(postSaveDTO.getAuthorId())
                        .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %d not found.", postSaveDTO.getAuthorId())));

        post.setAuthor(author);
        post.setCreationDate(LocalDateTime.now());
        post.setAttachments(attachments);
        Post savedPost = postRepository.save(post);
        valorationService.upvotePost(savedPost.getId(), author.getUserId());


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

        if(!post.getHasBeenEdited()){
            post.setTitle(postUpdateDTO.getTitle());
            post.setBody(postUpdateDTO.getBody());

            if (postUpdateDTO.getFileIdsToRemove() != null && !postUpdateDTO.getFileIdsToRemove().isEmpty()) {
                removeAttachments(post, postUpdateDTO.getFileIdsToRemove());
            }

            if (postUpdateDTO.getFilesToAdd() != null && !postUpdateDTO.getFilesToAdd().isEmpty()) {
                validateAttachmentsForUpdate(post, postUpdateDTO.getFilesToAdd());
                addAttachments(post, postUpdateDTO.getFilesToAdd());
            }
            post.setHasBeenEdited(true);
            postRepository.save(post);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePost(Long id, Long userId, String authorizationHeader) {
        LOGGER.info("Deleting post by id...");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));

        System.out.println(post);
        System.out.println(userId);

        boolean isValidUser = post.getAuthor().getUserId().equals(userId);
        List<Long> forumAdmins = authClient.getForumsAdmin(userId, authorizationHeader);
        Role userRole = jwtService.extractRole(authorizationHeader);

        System.out.println(forumAdmins);

        if ((!isValidUser && !forumAdmins.contains(post.getForumId())) && !userRole.equals(Role.ADMIN)) {
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

    @Override
    public Long getPostForumId(Long postId) {
        LOGGER.info("Getting forum id from post with id: {}" ,postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));


        LOGGER.info("FORUM ID TO DEBUG: {}", post.getForumId());
        return post.getForumId();
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

        UserDTO userDTO = userMapper.mapUserToUserDTO(post.getAuthor());
        postResponseDTO.setAuthor(userDTO);

        postResponseDTO.setLikedByUser(post.getUpvotesByUserId().contains(userId));
        postResponseDTO.setDislikedByUser(post.getDownvotesByUserId().contains(userId));
        postResponseDTO.setValoration(post.getValoration());
        postResponseDTO.setReportedByUser(post.getDeleteRequests()
                                                .stream()
                                                .anyMatch((DeleteRequest deleteRequest) -> deleteRequest.getReporterId().equals(userId)));
        return postResponseDTO;
    }
}
