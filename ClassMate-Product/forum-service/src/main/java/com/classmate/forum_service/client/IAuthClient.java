package com.classmate.forum_service.client;

import com.classmate.forum_service.dto.user.GetUserProfileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authentication-service")
public interface IAuthClient {
    @GetMapping("/api/profiles/{userId}")
    public GetUserProfileResponseDTO  getUserProfile(@PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader);
}
