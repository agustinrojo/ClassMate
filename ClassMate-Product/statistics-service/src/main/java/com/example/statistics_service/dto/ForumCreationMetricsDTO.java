package com.example.statistics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.YearMonth;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForumCreationMetricsDTO {

    private LocalDateTime yearMonth;
    private Long forumCount;

}
