package com.classmate.forum_service.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for creating or updating a forum.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForumRequestDTO {

    /**
     * The title of the forum.
     */
    private String title;

    /**
     * The description of the forum.
     */
    private String description;
}
