package com.classmate.post_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private Long forumId;
    private Long authorId;
    private String title;
    private String body;
    // Falta photo, attachments y valuation
    private LocalDateTime creationDate;
}
