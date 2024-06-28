package com.example.Security.repositories;

import com.example.Security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByForumsSubscribedContaining(Long forumId);

    List<User> findByForumsCreatedContaining(Long forumId);
}
