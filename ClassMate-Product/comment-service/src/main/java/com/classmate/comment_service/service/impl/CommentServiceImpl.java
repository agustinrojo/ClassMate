package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.client.FileServiceClient;
import com.classmate.comment_service.dto.CommentDTORequest;
import com.classmate.comment_service.dto.CommentDTOResponse;
import com.classmate.comment_service.dto.CommentUpdateDTO;
import com.classmate.comment_service.dto.filedtos.FileDTO;
import com.classmate.comment_service.entity.Attachment;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.repository.ICommentRepository;
import com.classmate.comment_service.service.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the ICommentService interface.
 */
@Service
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository ICommentRepository;
    private final CommentMapper commentMapper;
    private final FileServiceClient fileServiceClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    /**
     * Constructs a new CommentServiceImpl with the specified repository and mapper.
     *
     * @param ICommentRepository the comment repository
     * @param commentMapper the comment mapper
     */
    public CommentServiceImpl(com.classmate.comment_service.repository.ICommentRepository ICommentRepository, CommentMapper commentMapper, FileServiceClient fileServiceClient) {
        this.ICommentRepository = ICommentRepository;
        this.commentMapper = commentMapper;
        this.fileServiceClient = fileServiceClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDTORequest getCommentById(Long id)  {
        LOGGER.info("Getting comment by id...");
        Comment comment = ICommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        List<FileDTO> files = new ArrayList<>();

        return commentMapper.mapToCommentDTO(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommentDTORequest> getCommentsByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = ICommentRepository.findByPostId(postId, pageable);
        return commentsPage.getContent().stream()
                .map(commentMapper::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDTOResponse saveComment(CommentDTORequest commentRequestDTO) {
        validateComment(commentRequestDTO.getBody());
        validateAttachments(commentRequestDTO.getFiles());
        List<Attachment> attachments = new ArrayList<Attachment>();
        if(!commentRequestDTO.getFiles().isEmpty()){
            List<MultipartFile> files = commentRequestDTO.getFiles();
            for (MultipartFile file: files){
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
        Comment savedComment = ICommentRepository.save(comment);
        return commentMapper.mapToCommentDTOResponse(savedComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateComment(Long id, CommentUpdateDTO commentUpdateDTO) {
        validateComment(commentUpdateDTO.getBody());
        LOGGER.info("Updating comment...");
        Comment comment = ICommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        comment.setBody(commentUpdateDTO.getBody());
        ICommentRepository.save(comment);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteComment(Long id, Long userId) {
        LOGGER.info("Deleting comment...");
        Comment comment = ICommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }
        ICommentRepository.delete(comment);
    }

    /**
     * Validates the comment data.
     *
     * @param body the comment body to validate
     * @throws InvalidCommentException if the comment is invalid
     */
    private void validateComment(String body) {
        if (body == null || body.isEmpty()) {
            throw new InvalidCommentException("Comment body cannot be empty");
        }
        if (body.length() > 2000) {
            throw new InvalidCommentException("Comment body cannot exceed 2000 characters");
        }
    }

    private void validateAttachments(List<MultipartFile> files){
        if (files.size() > 3) {
            throw new IllegalArgumentException("No more than 3 attachments allowed.");
        }
        for(MultipartFile file : files){
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("File size should not exceed 10MB.");
            }
        }
    }

}
