package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.client.FileServiceClient;
import com.classmate.comment_service.dto.CommentDTORequest;
import com.classmate.comment_service.dto.CommentDTOResponse;
import com.classmate.comment_service.dto.CommentUpdateDTO;
import com.classmate.comment_service.dto.CommentDeletionDTO;
import com.classmate.comment_service.dto.filedtos.FileDeletionDTO;
import com.classmate.comment_service.dto.notifications.CommentNotificationEventDTO;
import com.classmate.comment_service.dto.user.UserDTO;
import com.classmate.comment_service.entity.Attachment;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.entity.User;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.publisher.CommentPublisher;
import com.classmate.comment_service.repository.ICommentRepository;
import com.classmate.comment_service.mapper.IUserMapper;
import com.classmate.comment_service.repository.IUserRepository;
import com.classmate.comment_service.service.ICommentService;
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
    private final FileServiceClient fileServiceClient;
    private final CommentPublisher commentPublisher;
    private final IUserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    public CommentServiceImpl(ICommentRepository commentRepository, CommentMapper commentMapper, IUserMapper userMapper, FileServiceClient fileServiceClient, CommentPublisher commentPublisher, IUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.fileServiceClient = fileServiceClient;
        this.commentPublisher = commentPublisher;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDTOResponse getCommentById(Long id, Long userId) {
        LOGGER.info("Getting comment by id...");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

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
    public CommentDTOResponse saveComment(CommentDTORequest commentRequestDTO) {

        validateComment(commentRequestDTO.getBody());
        validateAttachments(commentRequestDTO.getFiles());

        LOGGER.info("Saving comment...");

        List<Attachment> attachments = uploadFiles(commentRequestDTO.getFiles());

        Comment comment = commentMapper.mapToComment(commentRequestDTO);
        comment.setAttachments(attachments);
        comment.addUpvote(commentRequestDTO.getAuthorId());

        User user = userRepository.findById(commentRequestDTO.getAuthorId()).orElseThrow();
        comment.setAuthor(user);

        Comment savedComment = commentRepository.save(comment);

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
    public void deleteComment(Long id, Long userId) {
        LOGGER.info("Deleting comment...");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }

        List<Long> attachmentIds = comment.getAttachments().stream()
                        .map(Attachment :: getId)
                        .toList();

        CommentDeletionDTO event = new CommentDeletionDTO(attachmentIds);
        commentPublisher.publishCommentDeleteEvent(event);

        commentRepository.delete(comment);
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
        return commentResponseDTO;
    }
}
