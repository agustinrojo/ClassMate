package com.example.notification_service.dto.publisherAndConsumerDTOS.valoration;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneReachedEventDTO {
    private Long postId;
    private Long authorId;
    private Long forumId;
    private int milestone;
    private String title;
}

