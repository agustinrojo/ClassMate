package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.client.FileServiceClient;
import com.classmate.comment_service.dto.CommentDTORequest;
import com.classmate.comment_service.dto.CommentDTOResponse;
import com.classmate.comment_service.dto.CommentUpdateDTO;
import com.classmate.comment_service.dto.filedtos.FileDeletionDTO;
import com.classmate.comment_service.entity.Attachment;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.publisher.CommentPublisher;
import com.classmate.comment_service.repository.ICommentRepository;
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
    private final FileServiceClient fileServiceClient;
    private final CommentPublisher commentPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    public CommentServiceImpl(ICommentRepository commentRepository, CommentMapper commentMapper, FileServiceClient fileServiceClient, CommentPublisher commentPublisher) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.fileServiceClient = fileServiceClient;
        this.commentPublisher = commentPublisher;
    }

    @Override
    public CommentDTOResponse getCommentById(Long id) {
        LOGGER.info("Getting comment by id...");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return commentMapper.mapToCommentDTOResponse(comment);
    }

    @Override
    public List<CommentDTOResponse> getCommentsByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);
        return commentsPage.getContent().stream()
                .map(commentMapper::mapToCommentDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTOResponse saveComment(CommentDTORequest commentRequestDTO) {
        validateComment(commentRequestDTO.getBody());
        validateAttachments(commentRequestDTO.getFiles());

        LOGGER.info("Saving comment...");

        List<Attachment> attachments = new ArrayList<>();
        if (!commentRequestDTO.getFiles().isEmpty()) {
            List<MultipartFile> files = commentRequestDTO.getFiles();
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

        Comment comment = commentMapper.mapToComment(commentRequestDTO);
        comment.setAttachments(attachments);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToCommentDTOResponse(savedComment);
    }

    @Override
    public void updateComment(Long id, CommentUpdateDTO commentUpdateDTO) {
        validateComment(commentUpdateDTO.getBody());
        LOGGER.info("Updating comment...");

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        comment.setBody(commentUpdateDTO.getBody());

        if (commentUpdateDTO.getFilesToAdd() != null && !commentUpdateDTO.getFilesToAdd().isEmpty()) {
            addAttachments(comment, commentUpdateDTO.getFilesToAdd());
        }
        if (commentUpdateDTO.getFileIdsToRemove() != null && !commentUpdateDTO.getFileIdsToRemove().isEmpty()) {
            removeAttachments(comment, commentUpdateDTO.getFileIdsToRemove());
        }

        commentRepository.save(comment);
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
        comment.removeAttachments(fileIdsToRemove);

        for (Long fileId : fileIdsToRemove) {
            FileDeletionDTO event = new FileDeletionDTO(fileId);
            commentPublisher.publishFileDeleteEvent(event);
        }
    }

    @Override
    public void deleteComment(Long id, Long userId) {
        LOGGER.info("Deleting comment...");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }
        commentRepository.delete(comment);
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
}
