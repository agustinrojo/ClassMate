package com.classmate.forum_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object for API responses that include a forum and its posts.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseDTO {

    /**
     * The forum data.
     */
    private ForumResponseDTO forum;

    /**
     * The list of posts associated with the forum.
     */
    private List<PostResponseDTO> posts;

    private boolean isCreator;

    private boolean isAdmin;

}