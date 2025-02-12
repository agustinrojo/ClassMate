package com.example.statistics_service.service.impl;

import com.example.statistics_service.dto.ForumCreationMetricsDTO;
import com.example.statistics_service.entity.ForumCreationStats;
import com.example.statistics_service.repository.IForumCreationStatsRepository;
import com.example.statistics_service.service.IForumCreationStatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumCreationStatsService implements IForumCreationStatsService {

    private final IForumCreationStatsRepository forumCreationStatsRepository;

    public ForumCreationStatsService(IForumCreationStatsRepository forumCreationStatsRepository) {
        this.forumCreationStatsRepository = forumCreationStatsRepository;
    }

    // Method to handle forum creation event
    @Override
    public void handleForumCreatedEvent() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Find or create the statistics for the current datetime
        ForumCreationStats stats = forumCreationStatsRepository
                .findByYearMonth(currentDateTime)
                .orElse(new ForumCreationStats(currentDateTime, 0L));

        // Increment the forum count for the current date
        stats.setForumCount(stats.getForumCount() + 1);

        // Save the updated stats
        forumCreationStatsRepository.save(stats);
    }

    // Get monthly statistics for a specific year
    @Override
    public List<ForumCreationMetricsDTO> getMonthlyStatsForYear(int year) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0, 0);  // Start of the year
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 58, 999999999);  // End of the year

        List<ForumCreationStats> stats = forumCreationStatsRepository.findByYearMonthBetween(startOfYear, endOfYear);

        return stats.stream()
                .map(stat -> new ForumCreationMetricsDTO(stat.getYearMonth(), stat.getForumCount()))
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalForumCount() {
        return forumCreationStatsRepository.findAll().stream()
                .mapToLong(ForumCreationStats::getForumCount)
                .sum();
    }
}
