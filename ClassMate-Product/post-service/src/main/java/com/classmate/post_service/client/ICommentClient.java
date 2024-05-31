package com.classmate.post_service.client;

import com.classmate.post_service.dto.CommentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "comment-service")
public interface ICommentClient {

    @GetMapping(("/api/comments/post/{postId}"))
    List<CommentDTO> getCommentsByPostId(@RequestParam("postId") Long postId,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size);
}
