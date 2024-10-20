package com.classmate.comment_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "post-service")
public interface IPostClient {

    @GetMapping("/api/posts/{postId}/forumId")
    Long getPostForumId(@PathVariable Long postId);
}

