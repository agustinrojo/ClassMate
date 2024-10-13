package com.classmate.post_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "authentication-service")
public interface IAuthClient {
    @GetMapping("/api/users/{userId/forumsAdmin}")
    List<Long> getForumsAdmin(@PathVariable Long userId);
}
