package com.classmate.post_service.service;

import com.classmate.post_service.dto.APIResponseDTO;
import com.classmate.post_service.dto.PostDTO;

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
    APIResponseDTO getPostById(Long id);

    /**
     * Retrieves posts by name with pagination support.
     *
     * @param name the name to search for
     * @param page the page number to retrieve
     * @param size the number of posts per page
     * @return a list of PostDTOs matching the search criteria
     */
    List<PostDTO> getPostsByName(String name, int page, int size);

    /**
     * Retrieves posts by forum ID with pagination support.
     *
     * @param forumId the forum ID
     * @param page the page number to retrieve
     * @param size the number of posts per page
     * @return a list of PostDTOs belonging to the specified forum
     */
    List<PostDTO> getPostsByForumId(Long forumId, int page, int size);

    /**
     * Saves a new post.
     *
     * @param postDTO the post data to save
     * @return the saved PostDTO
     */
    PostDTO savePost(PostDTO postDTO);

    /**
     * Updates an existing post.
     *
     * @param id the ID of the post to update
     * @param postDTO the updated post data
     */
    void updatePost(Long id, PostDTO postDTO);

    /**
     * Deletes a post by its ID.
     *
     * @param id the ID of the post to delete
     * @param userId the ID of the user attempting to delete the post
     */
    void deletePost(Long id, Long userId);
}
