package com.classmate.post_service.controller;

import com.classmate.post_service.dto.*;
import com.classmate.post_service.service.IPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }


    /**
     * Get a post by its ID along with its comments.
     * @param id the ID of the post
     * @return the post along with its comments
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO> getPostById(@PathVariable Long id) {
        APIResponseDTO apiResponseDTO = postService.getPostById(id);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    /**
     * Get posts by their name.
     * @param name the name of the post
     * @param page the page number to retrieve
     * @param size the number of posts per page
     * @return a list of posts with the given name
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> getPostsByName(@RequestParam String name,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        List<PostDTO> posts = postService.getPostsByName(name, page, size);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * Get posts by the forum ID.
     * @param forumId the ID of the forum
     * @param page the page number to retrieve
     * @param size the number of posts per page
     * @return a list of posts in the given forum
     */
    @GetMapping("/forum/{forumId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByForumId(@PathVariable Long forumId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<PostResponseDTO> posts = postService.getPostsByForumId(forumId, page, size);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/isAuthor/{postId}/{authorId}")
    public ResponseEntity<IsPostAuthorDTO> isPostAuthor(@PathVariable("postId") Long postId,
                                                        @PathVariable("authorId") Long authorId){
        IsPostAuthorDTO isPostAuthorDTO = postService.isPostAuthor(postId, authorId);
        return new ResponseEntity<>(isPostAuthorDTO, HttpStatus.OK);
    }

    @GetMapping("/forumsSubscribed")
    public ResponseEntity<List<PostResponseDTO>> getPostBySubscribedForums(@RequestBody RequestByForumsDTO requestByForumsDTO,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size){
        List<PostResponseDTO> posts =postService.getPostsBySubscribedForums(requestByForumsDTO, page, size);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * Create a new post.
     * @param postRequestDTO the post to create
     * @return the created post
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> savePost(@ModelAttribute PostRequestDTO postRequestDTO) {
        PostResponseDTO savedPost = postService.savePost(postRequestDTO);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    /**
     * Update an existing post.
     * @param id the ID of the post to update
     * @param postUpdateDTO the updated post data
     * @return a response indicating success or failure
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
                                           @ModelAttribute PostUpdateDTO postUpdateDTO) {
        postService.updatePost(id, postUpdateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } // Enviar: title, body, filesToAdd, filesIdToRemove

    /**
     * Delete a post by its ID.
     * @param id the ID of the post to delete
     * @param userId the ID of the user attempting to delete the comment
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
