package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.client.FileServiceClient;
import com.classmate.comment_service.client.IPostClient;
import com.classmate.comment_service.dto.*;
import com.classmate.comment_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.comment_service.dto.delete_request.DeleteRequestDTOResponse;
import com.classmate.comment_service.dto.filedtos.FileDeletionDTO;
import com.classmate.comment_service.dto.notifications.CommentNotificationEventDTO;
import com.classmate.comment_service.dto.user.UserDTO;
import com.classmate.comment_service.entity.Attachment;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.entity.DeleteRequest;
import com.classmate.comment_service.entity.User;
import com.classmate.comment_service.entity.enums.Role;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.AttachmentMapper;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.mapper.DeleteRequestMapper;
import com.classmate.comment_service.publisher.CommentPublisher;
import com.classmate.comment_service.repository.ICommentRepository;
import com.classmate.comment_service.mapper.IUserMapper;
import com.classmate.comment_service.repository.IUserRepository;
import com.classmate.comment_service.service.ICommentService;
import com.classmate.comment_service.service.ICommentValorationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final IUserMapper userMapper;
    private final DeleteRequestMapper deleteRequestMapper;
    private final AttachmentMapper attachmentMapper;
    private final FileServiceClient fileServiceClient;
    private final IPostClient postClient;
    private final CommentPublisher commentPublisher;
    private final IUserRepository userRepository;
    private final ICommentValorationService valorationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    public CommentServiceImpl(ICommentRepository commentRepository, CommentMapper commentMapper, IUserMapper userMapper, DeleteRequestMapper deleteRequestMapper, AttachmentMapper attachmentMapper, FileServiceClient fileServiceClient, IPostClient postClient, CommentPublisher commentPublisher, IUserRepository userRepository, ICommentValorationService valorationService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.deleteRequestMapper = deleteRequestMapper;
        this.attachmentMapper = attachmentMapper;
        this.fileServiceClient = fileServiceClient;
        this.postClient = postClient;
        this.commentPublisher = commentPublisher;
        this.userRepository = userRepository;
        this.valorationService = valorationService;
    }

    @Override
    public CommentDTOResponse getCommentById(Long id, Long userId) {
        LOGGER.info("Getting comment by id...");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        LOGGER.info("Comment forum ID after retrieving: {}", comment.getForumId());
        CommentDTOResponse commentDTOResponse = commentMapper.mapToCommentDTOResponse(comment);

        UserDTO userDTO = userMapper.mapUserToUserDTO(comment.getAuthor());
        commentDTOResponse.setAuthor(userDTO);

        commentDTOResponse.setLikedByUser(comment.getUpvotesByUserId().contains(userId));
        commentDTOResponse.setDislikedByUser(comment.getDownvotesByUserId().contains(userId));
        commentDTOResponse.setValoration(comment.getValoration());
        return commentDTOResponse;
    }

    @Override
    public List<CommentDTOResponse> getCommentsByPostId(Long postId, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);

        return commentsPage.getContent().stream()
                .map(comment -> getCommentResponseDTO(comment, userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTOResponse saveComment(CommentDTORequest commentRequestDTO) {
        LOGGER.info("Saving comment...");

        validateComment(commentRequestDTO.getBody());
        validateAttachments(commentRequestDTO.getFiles());

        List<Attachment> attachments = uploadFiles(commentRequestDTO.getFiles());

        Comment comment = commentMapper.mapToComment(commentRequestDTO);
        comment.setAttachments(attachments);

        // Get forumId via Openfeign call
        Long commentForumId = postClient.getPostForumId(comment.getPostId());
        LOGGER.info("Comment forumId: {}", commentForumId);
        comment.setForumId(commentForumId);


        try {
            User user = userRepository.findById(commentRequestDTO.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            comment.setAuthor(user);
        } catch (Exception e) {
            LOGGER.error("Error fetching user: {}", e.getMessage(), e);
            throw e;  // Re-throw the exception or handle it appropriately
        }

        Comment savedComment = commentRepository.save(comment);

        if (savedComment.getAuthor() != null) {
            LOGGER.info("Comment author ID {}", savedComment.getAuthor().getUserId());
            valorationService.upvoteComment(savedComment.getId(), savedComment.getAuthor().getUserId());
        }

        CommentDTOResponse commentDTOResponse = commentMapper.mapToCommentDTOResponse(savedComment);
        commentDTOResponse.setLikedByUser(true);
        commentDTOResponse.setDislikedByUser(false);
        commentDTOResponse.setValoration(1);
        commentDTOResponse.setAuthor(userMapper.mapUserToUserDTO(savedComment.getAuthor()));

        // Publish notification event to notify post author
        CommentNotificationEventDTO commentNotificationEventDTO = new CommentNotificationEventDTO(
                savedComment.getPostId(),
                savedComment.getId(),
                savedComment.getAuthor().getUserId()
        );
        commentPublisher.publishCommentNotificationEvent(commentNotificationEventDTO);

        Long commentCount = getCommentCountByPostId(commentRequestDTO.getPostId());
        commentPublisher.publishCommentCountEvent(commentCount, commentRequestDTO.getPostId());

        return commentDTOResponse;
    }

    @Override
    public void updateComment(Long id, CommentUpdateDTO commentUpdateDTO) {
        LOGGER.info("Updating comment by id...");
        validateComment(commentUpdateDTO.getBody());

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        if(!comment.getHasBeenEdited()){
            comment.setBody(commentUpdateDTO.getBody());

            if (commentUpdateDTO.getFileIdsToRemove() != null && !commentUpdateDTO.getFileIdsToRemove().isEmpty()) {
                removeAttachments(comment, commentUpdateDTO.getFileIdsToRemove());
            }

            if (commentUpdateDTO.getFilesToAdd() != null && !commentUpdateDTO.getFilesToAdd().isEmpty()) {
                validateAttachmentsForUpdate(comment, commentUpdateDTO.getFilesToAdd());
                addAttachments(comment, commentUpdateDTO.getFilesToAdd());
            }
            comment.setHasBeenEdited(true);
            commentRepository.save(comment);
        }

    }


    @Override
    @Transactional
    public void deleteComment(Long id, Long userId, Role role) {
        LOGGER.info("Deleting comment...");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        LOGGER.info(String.format("ROLE %s", role.toString()));
        if (!comment.getAuthor().getUserId().equals(userId) && role != Role.ADMIN){
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }

        List<Long> attachmentIds = comment.getAttachments().stream()
                        .map(Attachment :: getId)
                        .toList();

        CommentDeletionDTO event = new CommentDeletionDTO(attachmentIds);
        commentPublisher.publishCommentDeleteEvent(event);

        Long commentCount = commentRepository.countByPostId(comment.getPostId());
        commentPublisher.publishCommentCountEvent(commentCount - 1, comment.getPostId());

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void reportComment(DeleteRequestDTO deleteRequest, Long commentId) {
        Comment reportedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String.format("Comment with id %d not found.", commentId)));

        DeleteRequest newDeleteRequest = deleteRequestMapper.mapToEntity(deleteRequest);

        reportedComment.addDeleteRequest(newDeleteRequest);
        commentRepository.save(reportedComment);
    }

    @Override
    public List<CommentDeleteRequestDTO> getReportedComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> reportedComments = commentRepository.findAllWithDeleteRequests(pageable);
        return reportedComments
                .stream()
                .map(this::mapToCommentDeleteRequestDTO)
                .toList();
    }


    public void addAttachments(Comment comment, List<MultipartFile> filesToAdd) {
        for (MultipartFile file : filesToAdd) {
            Long fileId = Objects.requireNonNull(fileServiceClient.uploadFile(file).getBody()).getFileId();
            Attachment attachment = Attachment.builder()
                    .id(fileId)
                    .originalFilename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();
            comment.addAttachment(attachment);
        }
    }

    public void removeAttachments(Comment comment, List<Long> fileIdsToRemove) {

        List<Long> validFileIdsToRemove = validateFileIdsToRemove(comment, fileIdsToRemove);

        // Remove the attachments from the existing list
        List<Attachment> remainingAttachments = comment.getAttachments().stream()
                .filter(attachment -> !validFileIdsToRemove.contains(attachment.getId()))
                .toList();
        comment.getAttachments().clear();
        comment.getAttachments().addAll(remainingAttachments);

        for (Long fileId : validFileIdsToRemove) {
            FileDeletionDTO event = new FileDeletionDTO(fileId);
            commentPublisher.publishFileDeleteEvent(event);
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

    private void validateComment(String body) {
        if (body == null || body.isEmpty()) {
            throw new InvalidCommentException("Comment body cannot be empty");
        }
        if (body.length() > 2000) {
            throw new InvalidCommentException("Comment body cannot exceed 2000 characters");
        }
    }

    private void validateAttachments(List<MultipartFile> files) {
        if (files.size() > 3) {
            throw new IllegalArgumentException("No more than 3 attachments allowed.");
        }
        for (MultipartFile file : files) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("File size should not exceed 10MB.");
            }
        }
    }

    private void validateAttachmentsForUpdate(Comment comment, List<MultipartFile> filesToAdd) {
        int totalAttachments = comment.getAttachments().size() + filesToAdd.size();
        if (totalAttachments > 3) {
            throw new IllegalArgumentException("No more than 3 attachments allowed.");
        }
        for (MultipartFile file : filesToAdd) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("File size should not exceed 10MB.");
            }
        }
    } // Siempre me apasionó el DRY

    public List<Long> validateFileIdsToRemove(Comment comment, List<Long> fileIdsToRemove) {
        return comment.getAttachments().stream()
                .filter(attachment -> fileIdsToRemove.contains(attachment.getId()))
                .map(Attachment :: getId)
                .toList();
    }

    public CommentDTOResponse getCommentResponseDTO(Comment comment, Long userId) {
        CommentDTOResponse commentResponseDTO = commentMapper.mapToCommentDTOResponse(comment);

        UserDTO userDTO = userMapper.mapUserToUserDTO(comment.getAuthor());
        commentResponseDTO.setAuthor(userDTO);

        commentResponseDTO.setLikedByUser(comment.getUpvotesByUserId().contains(userId));
        commentResponseDTO.setDislikedByUser(comment.getDownvotesByUserId().contains(userId));
        commentResponseDTO.setValoration(comment.getValoration());

        commentResponseDTO.setForumId(comment.getForumId());
        commentResponseDTO.setReportedByUser(comment.getDeleteRequests().stream()
                                                        .anyMatch((DeleteRequest del) -> del.getReporterId().equals(userId)));
        return commentResponseDTO;
    }

    private Long getCommentCountByPostId(Long postId){
        return commentRepository.countByPostId(postId);
    }

    private CommentDeleteRequestDTO mapToCommentDeleteRequestDTO(Comment comment){
        UserDTO userDTO = userMapper.mapUserToUserDTO(comment.getAuthor());
        List<DeleteRequestDTOResponse> deleteRequests = comment.getDeleteRequests()
                .stream()
                .map((DeleteRequest deleteRequest) -> {
                    User user = userRepository.findById(deleteRequest.getReporterId())
                            .orElseThrow(() -> new CommentNotFoundException(String.format("User with id: %d not found", deleteRequest.getReporterId())));
                    return DeleteRequestDTOResponse
                            .builder()
                            .reporterId(deleteRequest.getReporterId())
                            .message(deleteRequest.getMessage())
                            .reporterNickname(user.getNickname())
                            .build();
                })
                .toList();
        return CommentDeleteRequestDTO
                .builder()
                .deleteRequests(deleteRequests)
                .author(userDTO)
                .creationDate(comment.getCreationDate())
                .forumId(comment.getForumId())
                .hasBeenEdited(comment.getHasBeenEdited())
                .valoration(comment.getValoration())
                .files(comment.getAttachments().stream().map(attachmentMapper::toFileDTO).toList())
                .body(comment.getBody())
                .postId(comment.getPostId())
                .id(comment.getId())
                .build();

    }
}
