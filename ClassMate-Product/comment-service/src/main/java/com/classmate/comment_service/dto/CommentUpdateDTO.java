package com.classmate.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for updating Comment entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {

    /**
     * The body content of the comment.
     */
    private String body;
}
