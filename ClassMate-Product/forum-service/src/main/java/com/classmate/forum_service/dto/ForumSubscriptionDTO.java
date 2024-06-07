package com.classmate.forum_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for forum subscription actions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumSubscriptionDTO {
    private Long userId;
    private Long forumId;
}