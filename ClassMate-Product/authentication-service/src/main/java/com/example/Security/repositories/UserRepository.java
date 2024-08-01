package com.example.Security.repositories;

import com.example.Security.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByForumsSubscribedContaining(Long forumId);

    List<User> findByForumsCreatedContaining(Long forumId);

    @Query("SELECT u FROM User u JOIN u.userProfile up WHERE up.nickname = :nickname")
    List<User> findByUserProfileNickname(@Param("nickname") String nickname);

    @Query("SELECT u FROM User u WHERE u.userProfile.nickname LIKE %:nickname%")
    Page<User> findByUserProfileNicknameContaining(@Param("nickname") String nickname, Pageable pageable);
}
