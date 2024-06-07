package com.example.Security.repositories;

import com.example.Security.entities.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JWTTokenrepository extends JpaRepository<JWTToken, Long> {
    @Query("""
            SELECT t FROM JWTToken t
            INNER JOIN User u on t.user.id = u.id
            WHERE t.user.id = :userId and t.loggedOut = false
            """)
    List<JWTToken> findAllTokensByUser(@Param("userId") Long userId);

    Optional<JWTToken> findByToken(String token);
}
