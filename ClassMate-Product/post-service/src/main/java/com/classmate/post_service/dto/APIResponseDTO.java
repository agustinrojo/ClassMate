package com.classmate.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseDTO {
    private Long id;
    private Long forumId;
    private Long authorId;
    private String title;
    private String body;
    // Falta photo, attachments y valuation
    private LocalDateTime creationDate;
    private List<CommentDTO> commentDTOS;
}
