package com.classmate.forum_service.dto;

import com.classmate.forum_service.dto.user.UserDTO;
import com.classmate.forum_service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Data Transfer Object for the response containing forum information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForumResponseDTO {

    /**
     * The unique identifier of the forum.
     */
    private Long id;

    /**
     * The title of the forum.
     */
    private String title;

    /**
     * The description of the forum.
     */
    private String description;

    /**
     * The unique identifier of the creator of the forum.
     */
    private UserDTO creator;

    /**
     * The list of member IDs associated with the forum.
     */
    private Set<UserDTO> members;

    /**
     * The list of admin IDs associated with the forum.
     */
    private Set<UserDTO> admins;

    /**
     * The creation date and time of the forum.
     * This field is automatically populated.
     */
    private LocalDateTime creationDate;

    private Boolean hasBeenEdited;
}
