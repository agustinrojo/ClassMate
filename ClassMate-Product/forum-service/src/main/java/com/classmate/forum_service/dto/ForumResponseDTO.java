package com.classmate.forum_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long creatorId;

    /**
     * The list of member IDs associated with the forum.
     */
    private List<Long> memberIds;

    /**
     * The list of admin IDs associated with the forum.
     */
    private List<Long> adminIds;

    /**
     * The creation date and time of the forum.
     * This field is automatically populated.
     */
    private LocalDateTime creationDate;
}
