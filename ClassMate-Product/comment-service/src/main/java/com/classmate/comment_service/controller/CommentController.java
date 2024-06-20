package com.classmate.comment_service.controller;

import com.classmate.comment_service.dto.CommentDTORequest;
import com.classmate.comment_service.dto.CommentDTOResponse;
import com.classmate.comment_service.dto.CommentUpdateDTO;
import com.classmate.comment_service.service.ICommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for managing comments.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final ICommentService commentService;

    /**
     * Constructor to inject the comment service dependency.
     *
     * @param commentService the comment service to be injected
     */
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Endpoint to save a new comment.
     *
     * @param commentRequestDTO the data transfer object representing the comment to be saved
     * @return a response entity containing the saved comment and the HTTP status CREATED
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDTOResponse> saveComment(@ModelAttribute CommentDTORequest commentRequestDTO) {
        CommentDTOResponse savedComment = commentService.saveComment(commentRequestDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve a comment by its ID.
     *
     * @param id the ID of the comment to be retrieved
     * @return a response entity containing the retrieved comment and the HTTP status OK
     */
    @GetMapping("{id}")
    public ResponseEntity<CommentDTOResponse> getCommentById(@PathVariable("id") Long id) {
        CommentDTOResponse commentDTOResponse = commentService.getCommentById(id);
        return new ResponseEntity<>(commentDTOResponse, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve comments by post ID with pagination support.
     * This helps the frontend to load comments incrementally.
     *
     * Example usage:
     * - http://localhost:8080/api/comments/post/1?page=0&size=10
     * - http://localhost:8080/api/comments/post/1?page=1&size=10
     *
     * @param postId the ID of the post to retrieve comments for
     * @param page the page number to retrieve, default is 0
     * @param size the number of comments per page, default is 10
     * @return a response entity containing the list of comments and the HTTP status OK
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTOResponse>> getCommentsByPostId(@PathVariable("postId") Long postId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        List<CommentDTOResponse> comments = commentService.getCommentsByPostId(postId, page, size);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    /**
     * Endpoint to update an existing comment by its ID.
     *
     * @param id the ID of the comment to be updated
     * @param commentUpdateDTO the data transfer object containing the updated comment data
     * @return a response entity with the HTTP status NO_CONTENT
     */
    @PutMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateComment(@PathVariable("id") Long id,
                                              @ModelAttribute CommentUpdateDTO commentUpdateDTO) {
        commentService.updateComment(id, commentUpdateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to delete a comment by its ID.
     *
     * @param id the ID of the comment to be deleted
     * @param userId the ID of the user attempting to delete the comment
     * @return a response entity with the HTTP status NO_CONTENT
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id, @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
