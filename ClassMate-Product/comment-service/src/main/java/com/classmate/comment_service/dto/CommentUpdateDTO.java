package com.classmate.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Data Transfer Object for updating Comment entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {

    private String body;
    private List<MultipartFile> filesToAdd;
    private List<Long> fileIdsToRemove;
}
