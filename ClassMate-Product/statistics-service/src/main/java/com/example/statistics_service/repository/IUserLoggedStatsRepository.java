package com.example.statistics_service.repository;

import com.example.statistics_service.entity.LoggedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface IUserLoggedStatsRepository extends JpaRepository<LoggedUser, Long> {
    Long countByLoggedDateTimeAfter(LocalDateTime dateTime);
}
