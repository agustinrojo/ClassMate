package com.example.statistics_service.service;

import com.example.statistics_service.dto.ActivityResponseDTO;
import com.example.statistics_service.dto.TopForumDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IForumActivityService {

    List<ActivityResponseDTO> getAggregatedActivity(LocalDateTime startDate, LocalDateTime endDate);

    List<TopForumDTO> getTop5MostActiveForums();

}
