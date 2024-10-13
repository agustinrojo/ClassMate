package com.example.Security.controllers;


import com.example.Security.dto.user.profile.UserProfileResponseDTO;
import com.example.Security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId/forumsAdmin}")
    public ResponseEntity<List<Long>> getForumsAdmin(@PathVariable Long userId) {
        List<Long> usersAdmin = userService.getForumsAdmin(userId);

        return new ResponseEntity<>(usersAdmin, HttpStatus.OK);
    }
}
