package com.classmate.comment_service.repository;

import com.classmate.comment_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;


public interface IUserRepository extends JpaRepository<User, Long> {
}
