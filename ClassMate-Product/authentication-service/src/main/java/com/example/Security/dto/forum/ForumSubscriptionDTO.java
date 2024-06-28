package com.example.Security.dto.forum;

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
