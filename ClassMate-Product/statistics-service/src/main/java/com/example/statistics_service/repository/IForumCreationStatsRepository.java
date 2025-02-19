package com.example.statistics_service.repository;

import com.example.statistics_service.entity.ForumCreationStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface IForumCreationStatsRepository extends JpaRepository<ForumCreationStats, Long> {

    Optional<ForumCreationStats> findByYearMonth(LocalDateTime yearMonth);  // Query by Year-Month

    @Query("SELECT f FROM ForumCreationStats f WHERE f.yearMonth >= :startDate AND f.yearMonth <= :endDate")
    List<ForumCreationStats> findByYearMonthBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
