package com.classmate.post_service.client;

import com.classmate.post_service.dto.CommentDTO;
import com.classmate.post_service.dto.PostResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client interface for interacting with the Comment Service.
 * This client provides methods to fetch comments associated with a specific post.
 */
@FeignClient(name = "comment-service")
public interface ICommentClient {

    /**
     * Retrieves a list of comments associated with a specific post.
     *
     * @param postId the ID of the post for which comments are to be fetched
     * @param page the page number for pagination
     * @param size the number of comments per page
     * @return a list of CommentDTO objects representing the comments for the post
     */
    @GetMapping(("/api/comments/post/{postId}"))
    List<CommentDTO> getCommentsByPostId(@RequestParam("postId") Long postId,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size);
}
