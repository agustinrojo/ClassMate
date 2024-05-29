package com.classmate.comment_service.controller;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.service.ICommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO savedComment = commentService.saveComment(commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable("id") Long id) {
        CommentDTO commentDTO = commentService.getCommentById(id);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable("postId") Long postId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId, page, size);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateComment(@PathVariable("id") Long id, @RequestBody CommentDTO commentDTO) {
        commentService.updateComment(id, commentDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id, @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
