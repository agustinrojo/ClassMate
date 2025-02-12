package com.example.statistics_service.service;


import com.example.statistics_service.dto.ForumCreationMetricsDTO;

import java.util.List;

public interface IForumCreationStatsService {

    void handleForumCreatedEvent();

    List<ForumCreationMetricsDTO> getMonthlyStatsForYear(int year);

    Long getTotalForumCount();
}
