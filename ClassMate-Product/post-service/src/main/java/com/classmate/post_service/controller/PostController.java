    package com.classmate.post_service.controller;

    import com.classmate.post_service.dto.*;
    import com.classmate.post_service.dto.delete_request.DeleteRequestDTO;
    import com.classmate.post_service.dto.delete_request.PostDeleteRequestDTO;
    import com.classmate.post_service.service.IPostService;
    import com.classmate.post_service.service.IPostValorationService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/posts")
    public class PostController {
        private final IPostService postService;
        private final IPostValorationService valorationService;

        public PostController(IPostService postService, IPostValorationService valorationService) {
            this.postService = postService;
            this.valorationService = valorationService;
        }

        /**
         * Get a post by its ID along with its comments.
         * @param id the ID of the post
         * @return the post along with its comments
         */
        @GetMapping("/{id}")
        public ResponseEntity<APIResponseDTO> getPostById(@PathVariable Long id,
                                                          @RequestParam("userId") Long userId) {
            APIResponseDTO apiResponseDTO = postService.getPostById(id, userId);
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
        public ResponseEntity<List<PostResponseDTO>> getPostsByName(@RequestParam String name,
                                                            @RequestParam("userId") Long userId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
            List<PostResponseDTO> posts = postService.getPostsByName(name, userId, page, size);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        @GetMapping("/search/{forumId}")
        public ResponseEntity<List<PostResponseDTO>> getPostsByNameAndForumId(@PathVariable Long forumId,
                                                            @RequestParam String name,
                                                            @RequestParam("userId") Long userId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
            List<PostResponseDTO> posts = postService.getPostsByNameAndForumId(name, forumId, userId, page, size);
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
                                                               @RequestParam("userId") Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
            List<PostResponseDTO> posts = postService.getPostsByForumId(forumId, userId, page, size);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        @GetMapping("/{postId}/forumId")
        public ResponseEntity<Long> getPostForumId(@PathVariable Long postId) {
            Long postForumId = postService.getPostForumId(postId);
            return new ResponseEntity<>(postForumId, HttpStatus.OK);
        }

        @GetMapping("/isAuthor/{postId}/{authorId}")
        public ResponseEntity<IsPostAuthorDTO> isPostAuthor(@PathVariable("postId") Long postId,
                                                            @PathVariable("authorId") Long authorId){
            IsPostAuthorDTO isPostAuthorDTO = postService.isPostAuthor(postId, authorId);
            return new ResponseEntity<>(isPostAuthorDTO, HttpStatus.OK);
        }

        @GetMapping("/forumsSubscribed")
        public ResponseEntity<List<PostResponseDTO>> getPostBySubscribedForums(@RequestBody RequestByForumsDTO requestByForumsDTO,
                                                                               @RequestParam("userId") Long userId,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size){
            List<PostResponseDTO> posts = postService.getPostsBySubscribedForums(requestByForumsDTO, userId, page, size);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        @GetMapping("/author/{authorId}")
        public ResponseEntity<List<PostResponseDTO>> getPostsByAuthor(@PathVariable("authorId") Long authorId,
                                                                      @RequestParam("userId") Long userId,
                                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size){
            List<PostResponseDTO> postResponseDTOS = postService.getPostsByAuthorId(authorId, userId,page, size);
            return new ResponseEntity<>(postResponseDTOS, HttpStatus.OK);
        }

        @GetMapping("/reported")
        public ResponseEntity<List<PostDeleteRequestDTO>> getReportedPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
            List<PostDeleteRequestDTO> postDeleteRequests = postService.getReportedPosts(page, size);
            return new ResponseEntity<>(postDeleteRequests, HttpStatus.OK);
        }

        @GetMapping("/reported/{query}")
        public ResponseEntity<List<PostDeleteRequestDTO>> findReportedPostsByKeyword(@PathVariable("query") String query){
            List<PostDeleteRequestDTO> reportedPosts = postService.findReportedPostsByKeyword(query);
            return new ResponseEntity<>(reportedPosts, HttpStatus.OK);
        }

        /**
         * Create a new post.
         * @param postSaveDTO the post to create
         * @return the created post
         */
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<PostResponseDTO> savePost(@ModelAttribute PostSaveDTO postSaveDTO) {
            PostResponseDTO savedPost = postService.savePost(postSaveDTO);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        }

        @PostMapping("/{postId}/upvote")
        public ResponseEntity<Void> upvotePost(@PathVariable("postId") Long postId,
                                               @RequestParam("userId") Long userId){
            valorationService.upvotePost(postId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PostMapping("/{postId}/downvote")
        public ResponseEntity<Void> downvotePost(@PathVariable("postId") Long postId,
                                                 @RequestParam("userId") Long userId){
            valorationService.downvotePost(postId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PostMapping("/{postId}/removeVote")
        public ResponseEntity<Void> removeVote(@PathVariable("postId") Long postId,
                                                 @RequestParam("userId") Long userId){
            valorationService.removeVoteFromPost(postId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PostMapping("/report/{postId}")
        public ResponseEntity<Void> reportPost(@PathVariable("postId") Long postId,
                                               @RequestBody DeleteRequestDTO deleteRequestDTO){
            postService.reportPost(postId, deleteRequestDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

        @PutMapping("/absolve/{id}")
        public ResponseEntity<Void> absolvePost(@PathVariable("id") Long id){
            postService.absolvePost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        /**
         * Delete a post by its ID.
         * @param id the ID of the post to delete
         * @param userId the ID of the user attempting to delete the comment
         * @return a response indicating success or failure
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                               @RequestParam Long userId,
                                               @RequestHeader("Authorization") String authorizationHeader) {
            postService.deletePost(id, userId, authorizationHeader);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
