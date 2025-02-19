package com.example.statistics_service.repository;

import com.example.statistics_service.entity.UsersCreated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserCreatedStatsRepository extends JpaRepository<UsersCreated, Long> {
}
