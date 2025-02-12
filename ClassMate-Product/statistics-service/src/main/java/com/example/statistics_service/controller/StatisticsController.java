package com.example.statistics_service.controller;

import com.example.statistics_service.dto.ActivityResponseDTO;
import com.example.statistics_service.service.IForumActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class ForumActivityController {

    private final IForumActivityService forumActivityService;

    public ForumActivityController(IForumActivityService forumActivityService) {
        this.forumActivityService = forumActivityService;
    }

    @GetMapping("/aggregatedForumsActivity")
    public ResponseEntity<List<ActivityResponseDTO>> getAggregatedActivity(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ActivityResponseDTO> aggregatedActivity = forumActivityService.getAggregatedActivity(startDate, endDate);
        return ResponseEntity.ok(aggregatedActivity);
    }
}