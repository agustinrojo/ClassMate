package com.example.calendar_service.repository;

import com.example.calendar_service.entity.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITokenRepository extends JpaRepository<Token, Long> {
    boolean existsByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    List<Token> findByUserId(Long userId);
}
