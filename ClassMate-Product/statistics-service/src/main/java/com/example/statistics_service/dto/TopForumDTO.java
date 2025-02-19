package com.example.statistics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopForumDTO {
    private Long forumId;
    private Integer totalActivity;
}

