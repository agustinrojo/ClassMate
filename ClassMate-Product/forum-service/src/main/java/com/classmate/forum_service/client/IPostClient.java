package com.classmate.forum_service.client;

import com.classmate.forum_service.dto.PostResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client interface for interacting with the Post Service.
 * This client provides methods to fetch posts associated with a specific forum.
 */
@FeignClient(name = "post-service")
public interface IPostClient {

    /**
     * Retrieves a list of posts associated with a specific forum.
     *
     * @param forumId the ID of the forum for which posts are to be fetched
     * @param page the page number for pagination
     * @param size the number of posts per page
     * @return a list of PostDTO objects representing the posts for the forum
     */
    @GetMapping("/api/posts/forum/{forumId}")
    List<PostResponseDTO> getPostsByForumId(@PathVariable("forumId") Long forumId,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size);
}
