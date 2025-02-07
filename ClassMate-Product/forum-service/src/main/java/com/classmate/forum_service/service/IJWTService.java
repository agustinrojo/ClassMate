package com.classmate.forum_service.service;

import com.classmate.forum_service.entity.enums.Role;

public interface IJWTService {
    Role extractRole(String token);
}
