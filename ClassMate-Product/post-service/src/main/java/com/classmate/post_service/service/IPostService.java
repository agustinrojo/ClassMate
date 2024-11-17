package com.classmate.post_service.service;

import com.classmate.post_service.dto.*;

import java.util.List;

/**
 * Service interface for managing posts.
 */
public interface IPostService {

    /**
     * Retrieves a post by its ID along with its comments.
     *
     * @param id the ID of the post
     * @return the APIResponseDTO containing the post and its comments
     */
    APIResponseDTO getPostById(Long id, Long userId);

    /**
     * Retrieves posts by name with pagination support.
     *
     * @param name the name to search for
     * @param page the page number to retrieve
     * @param size the number of posts per page
     * @return a list of PostDTOs matching the search criteria
     */
    List<PostResponseDTO> getPostsByName(String name, Long userId, int page, int size);


    List<PostResponseDTO> getPostsByNameAndForumId(String name, Long forumId, Long userId, int page, int size);


    /**
     * Retrieves posts by forum ID with pagination support.
     *
     * @param forumId the forum ID
     * @param page the page number to retrieve
     * @param size the number of posts per page
     * @return a list of PostDTOs belonging to the specified forum
     */
    List<PostResponseDTO> getPostsByForumId(Long forumId, Long userId, int page, int size);


    /**
     * Saves a new post.
     *
     * @param postSaveDTO the post data to save
     * @return the saved PostDTO
     */
    PostResponseDTO savePost(PostSaveDTO postSaveDTO);

    /**
     * Updates an existing post.
     *
     * @param id the ID of the post to update

     */
    void updatePost(Long id, PostUpdateDTO postUpdateDTO);

    /**
     * Deletes a post by its ID.
     *
     * @param id the ID of the post to delete
     * @param userId the ID of the user attempting to delete the post
     */
    void deletePost(Long id, Long userId, String authorizationHeader);

    IsPostAuthorDTO isPostAuthor(Long postId, Long authorId);

    Long getPostForumId(Long postId);

    List<PostResponseDTO> getPostsBySubscribedForums(RequestByForumsDTO requestByForumsDTO, Long userId, int page, int size);
}
