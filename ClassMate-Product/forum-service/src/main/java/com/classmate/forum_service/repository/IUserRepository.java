package com.classmate.forum_service.repository;

import com.classmate.forum_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
}
