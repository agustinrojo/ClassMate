package com.example.Security.repositories;

import com.example.Security.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByForumsSubscribedContaining(Long forumId);

    List<User> findByForumsCreatedContaining(Long forumId);

    List<User> findByForumsAdminContaining(Long forumId);

    @Query("SELECT u FROM User u JOIN u.userProfile up WHERE up.nickname = :nickname")
    List<User> findByUserProfileNickname(@Param("nickname") String nickname);

    @Query("SELECT u FROM User u WHERE LOWER(u.userProfile.nickname) LIKE LOWER(CONCAT('%', :nickname, '%'))") // Modificado para ignoreCase
    Page<User> findByUserProfileNicknameContaining(@Param("nickname") String nickname, Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.userProfile WHERE u.id = :id")
    Optional<User> findByIdWithUserProfile(@Param("id") Long id);

}
