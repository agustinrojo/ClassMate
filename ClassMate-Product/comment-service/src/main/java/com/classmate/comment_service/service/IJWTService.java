package com.classmate.comment_service.service;

import com.classmate.comment_service.entity.enums.Role;



public interface IJWTService {
    Role extractRole(String token);
}
