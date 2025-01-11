package com.example.Security.repositories;

import com.example.Security.entities.UserReputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReputationRepository extends JpaRepository<UserReputation, Long> {
}
