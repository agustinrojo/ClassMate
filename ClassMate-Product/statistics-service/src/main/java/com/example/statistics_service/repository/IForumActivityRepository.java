package com.example.statistics_service.repository;

import com.example.statistics_service.entity.ForumActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IForumActivityRepository extends JpaRepository<ForumActivity, Long> {

    @Query("SELECT fa FROM ForumActivity fa WHERE fa.timestamp BETWEEN :startDate AND :endDate ORDER BY fa.timestamp")
    List<ForumActivity> findAllActivitiesBetween(LocalDateTime startDate, LocalDateTime endDate);
}

