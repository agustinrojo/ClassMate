package com.example.statistics_service.service.impl;

import com.example.statistics_service.dto.ActivityResponseDTO;
import com.example.statistics_service.entity.ForumActivity;
import com.example.statistics_service.repository.IForumActivityRepository;
import com.example.statistics_service.service.IForumActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForumActivityService implements IForumActivityService {
    private final IForumActivityRepository repository;

    public ForumActivityService(IForumActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ActivityResponseDTO> getAggregatedActivity(LocalDateTime startDate, LocalDateTime endDate) {
        List<ForumActivity> activities = repository.findAllActivitiesBetween(startDate, endDate);

        Map<LocalDate, Integer> aggregatedData = activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> activity.getTimestamp().toLocalDate(), // Group by date
                        Collectors.summingInt(ForumActivity::getActivityCount) // Sum activity counts
                ));

        // Convert to a list of ActivityResponse for the frontend
        return aggregatedData.entrySet().stream()
                .map(entry -> new ActivityResponseDTO(entry.getKey().atStartOfDay(), entry.getValue()))
                .sorted(Comparator.comparing(ActivityResponseDTO::getTimeBucket))
                .collect(Collectors.toList());
    }

}
