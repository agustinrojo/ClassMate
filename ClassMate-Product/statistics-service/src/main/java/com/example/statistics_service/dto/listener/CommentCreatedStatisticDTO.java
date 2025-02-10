package com.example.statistics_service.dto.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedStatisticDTO {
    private Long commentId;
    private Long forumId;
    private LocalDateTime timestamp;
}
