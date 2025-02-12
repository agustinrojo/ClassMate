package com.example.statistics_service.controller;

import com.example.statistics_service.dto.ActivityResponseDTO;
import com.example.statistics_service.dto.ForumCreationMetricsDTO;
import com.example.statistics_service.service.IForumActivityService;
import com.example.statistics_service.service.IForumCreationStatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final IForumActivityService forumActivityService;

    private final IForumCreationStatsService forumCreationStatsService;

    public StatisticsController(IForumActivityService forumActivityService, IForumCreationStatsService forumCreationStatsService) {
        this.forumActivityService = forumActivityService;
        this.forumCreationStatsService = forumCreationStatsService;
    }


    @GetMapping("/aggregatedForumsActivity")
    public ResponseEntity<List<ActivityResponseDTO>> getAggregatedActivity(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ActivityResponseDTO> aggregatedActivity = forumActivityService.getAggregatedActivity(startDate, endDate);
        return ResponseEntity.ok(aggregatedActivity);
    }

    @GetMapping("/monthlyCreation")
    public ResponseEntity<List<ForumCreationMetricsDTO>> getMonthlyForumCreation(@RequestParam int year) {
        List<ForumCreationMetricsDTO> metrics = forumCreationStatsService.getMonthlyStatsForYear(year);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/totalForums")
    public ResponseEntity<Long> getTotalForumsCreated() {
        Long totalForums = forumCreationStatsService.getTotalForumCount();
        return ResponseEntity.ok(totalForums);
    }


}