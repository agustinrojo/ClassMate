package com.classmate.post_service.repository;

import com.classmate.post_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
}
