package com.classmate.post_service.service;
import com.classmate.post_service.entity.enums.Role;

public interface IJWTService {
    Role extractRole(String token);
}
