package com.classmate.forum_service.controller;

import com.classmate.forum_service.dto.*;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.service.IForumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing forums.
 */
@RestController
@RequestMapping("/api/forums")
public class ForumController {

    private final IForumService forumService;

    /**
     * Constructs a new ForumController with the specified forum service.
     *
     * @param forumService the forum service
     */
    public ForumController(IForumService forumService) {
        this.forumService = forumService;
    }

    /**
     * Retrieves all forums.
     *
     * @return a list of all forums
     */
    @GetMapping
    public ResponseEntity<List<ForumResponseDTO>> getAllForums() {
        List<ForumResponseDTO> forums = forumService.getAllForums();
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    /**
     * Retrieves a forum by its ID along with its posts.
     *
     * @param id the ID of the forum
     * @return the forum along with its posts
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO> getForumById(@PathVariable Long id,
                                                       @RequestParam("userId") Long userId) {
        APIResponseDTO apiResponseDTO = forumService.getForumById(id, userId);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/multiple-forums")
    private ResponseEntity<List<ForumResponseDTO>> getMultipleForumsByIds(@RequestParam List<Long> ids,
                                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                                          @RequestParam(value = "size", defaultValue = "10") int size){
        List<ForumResponseDTO> forumResponseDTOS = forumService.getMultipleForumsByIds(ids, page, size);
        return ResponseEntity.ok(forumResponseDTOS);
    }

    @GetMapping("/sidebarData/{id}")
    public ResponseEntity<ForumSidebarDataDTO> getForumDataSidebarById(@PathVariable Long id) {
        ForumSidebarDataDTO forumSidebarDataDTO = forumService.getForumSidebarDataById(id);
        return new ResponseEntity<>(forumSidebarDataDTO, HttpStatus.OK);
    }

    /**
     * Retrieves forums by their title.
     *
     * @param title the title of the forum
     * @param page the page number to retrieve
     * @param size the number of forums per page
     * @return a list of forums with the given title
     */
    @GetMapping("/search")
    public ResponseEntity<List<ForumResponseDTO>> getForumsByTitle(@RequestParam String title,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        List<ForumResponseDTO> forums = forumService.getForumsByTitle(title, page, size);
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ForumExistsDTO> forumExists(@PathVariable("id") Long forumId){
        ForumExistsDTO forumExistsDTO = forumService.forumExists(forumId);
        return new ResponseEntity<>(forumExistsDTO, HttpStatus.OK);
    }

    @GetMapping("/isCreator/{forumId}")
    public ResponseEntity<IsForumCreatorDTO> isForumCreator(@PathVariable("forumId") Long forumId,
                                                            @RequestParam("userId") Long userId) {
        IsForumCreatorDTO isForumCreatorDTO = forumService.isForumCreator(forumId, userId);
        return new ResponseEntity<>(isForumCreatorDTO, HttpStatus.OK);
    }

    /**
     * Creates a new forum.
     *
     * @param forumRequestDTO the forum to create
     * @param creatorId the ID of the user creating the forum
     * @return the created forum
     */
    @PostMapping
    public ResponseEntity<ForumResponseDTO> saveForum(@RequestBody ForumRequestDTO forumRequestDTO,
                                                      @RequestParam Long creatorId) {
        ForumResponseDTO savedForum = forumService.saveForum(forumRequestDTO, creatorId);
        return new ResponseEntity<>(savedForum, HttpStatus.CREATED);
    }

    /**
     * Updates an existing forum.
     *
     * @param id the ID of the forum to update
     * @param forumRequestDTO the updated forum data
     * @return a response indicating success or failure
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateForum(@PathVariable Long id, @RequestBody ForumRequestDTO forumRequestDTO) {
        forumService.updateForum(id, forumRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a forum by its ID.
     *
     * @param id the ID of the forum to delete
     * @param userId the ID of the user attempting to delete the forum
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long id, @RequestParam Long userId) {
        forumService.deleteForum(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Adds a member to a forum.
     *
     * @param forumId the ID of the forum
     * @param memberId the ID of the member to add
     * @return a response indicating success or failure
     */
    @PostMapping("/{forumId}/members/{memberId}")
    public ResponseEntity<Void> addMember(@PathVariable Long forumId, @PathVariable Long memberId) {
        forumService.addMember(forumId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Adds an admin to a forum.
     *
     * @param forumId the ID of the forum
     * @param adminId the ID of the admin to add
     * @return a response indicating success or failure
     */
    @PostMapping("/{forumId}/admins/{adminId}")
    public ResponseEntity<Void> addAdmin(@PathVariable Long forumId, @PathVariable Long adminId) {
        forumService.addAdmin(forumId, adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Removes a member from a forum.
     *
     * @param forumId the ID of the forum
     * @param memberId the ID of the member to remove
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{forumId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long forumId, @PathVariable Long memberId) {
        forumService.removeMember(forumId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Removes an admin from a forum.
     *
     * @param forumId the ID of the forum
     * @param adminId the ID of the admin to remove
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{forumId}/admins/{adminId}")
    public ResponseEntity<Void> removeAdmin(@PathVariable Long forumId, @PathVariable Long adminId) {
        forumService.removeAdmin(forumId, adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Ban a user from the forum.
     *
     * @param forumId the ID of the forum
     * @param bannerId the ID of the user who is banning
     * @param bannedId the ID of the user to be banned
     * @return HTTP response indicating success or failure
     */
    @PostMapping("/{forumId}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long forumId,
                                        @RequestParam Long bannerId,
                                        @RequestParam Long bannedId) {
        forumService.banUser(forumId, bannerId, bannedId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
