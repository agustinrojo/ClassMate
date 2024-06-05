package com.classmate.forum_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumSubscriptionDTO {
    @JsonProperty("userId")
    Long userId;
    @JsonProperty("forumId")
    Long forumId;
}
