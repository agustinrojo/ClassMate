package com.example.statistics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ActivityResponseDTO {
    private LocalDateTime timeBucket;
    private int totalActivity;

}
