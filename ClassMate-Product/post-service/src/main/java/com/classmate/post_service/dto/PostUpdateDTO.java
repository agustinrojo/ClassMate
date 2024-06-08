package com.classmate.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for updating Post entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDTO {

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The body content of the post.
     */
    private String body;
}
