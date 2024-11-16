package com.classmate.forum_service.service;

import com.classmate.forum_service.dto.*;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.dto.user.UserDTO;

import java.util.List;

/**
 * Service interface for managing forums.
 */
public interface IForumService {

    /**
     * Retrieves all forums.
     *
     * @return a list of all forums
     */
    List<ForumResponseDTO> getAllForums();

    /**
     * Retrieves a forum by its ID along with its posts.
     *
     * @param id the ID of the forum
     * @return the forum along with its posts
     */
    APIResponseDTO getForumById(Long id, Long userId);

    List<ForumResponseDTO> getMultipleForumsByIds(List<Long> ids, int page, int size);

    ForumSidebarDataDTO getForumSidebarDataById(Long id);

    /**
     * Retrieves forums by their title.
     *
     * @param title the title of the forum
     * @param page the page number to retrieve
     * @param size the number of forums per page
     * @return a list of forums with the given title
     */
    List<ForumResponseDTO> getForumsByTitle(String title, int page, int size);

    /**
     * Creates a new forum.
     *
     * @param forumRequestDTO the forum to create
     * @param creatorId the ID of the user creating the forum
     * @return the created forum
     */
    ForumResponseDTO saveForum(ForumRequestDTO forumRequestDTO, Long creatorId);

    /**
     * Updates an existing forum.
     *
     * @param id the ID of the forum to update
     * @param forumRequestDTO the updated forum data
     */
    void updateForum(Long id, ForumRequestDTO forumRequestDTO);

    /**
     * Deletes a forum by its ID.
     *
     * @param id the ID of the forum to delete
     * @param userId the ID of the user attempting to delete the forum
     */
    void deleteForum(Long id, Long userId);

    /**
     * Adds a member to a forum.
     *
     * @param forumId the ID of the forum
     * @param memberId the ID of the member to add
     */
    void addMember(Long forumId, Long memberId);

    /**
     * Adds an admin to a forum.
     *
     * @param forumId the ID of the forum
     * @param adminId the ID of the admin to add
     */
    void addAdmin(Long forumId, Long adminId);

    /**
     * Removes a member from a forum.
     *
     * @param forumId the ID of the forum
     * @param memberId the ID of the member to remove
     */
    void removeMember(Long forumId, Long memberId);

    /**
     * Removes an admin from a forum.
     *
     * @param forumId the ID of the forum
     * @param adminId the ID of the admin to remove
     */
    void removeAdmin(Long forumId, Long adminId);

    ForumExistsDTO forumExists(Long id);

    IsForumCreatorDTO isForumCreator(Long forumId , Long userId);

    void banUser(Long forumId, Long bannerId, Long bannedId);

}
