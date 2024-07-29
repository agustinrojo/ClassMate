package com.example.chat_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u " +
           "WHERE u.id != :loggedUserId")
    List<User> findAllUsersExceptTheLoggedOne(Long loggedUserId);
}
