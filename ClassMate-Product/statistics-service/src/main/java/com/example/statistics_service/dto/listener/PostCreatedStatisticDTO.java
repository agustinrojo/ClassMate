package com.example.statistics_service.dto.listener;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreatedStatisticDTO {
    private Long postId;
    private Long forumId;
    private LocalDateTime timestamp;
}
