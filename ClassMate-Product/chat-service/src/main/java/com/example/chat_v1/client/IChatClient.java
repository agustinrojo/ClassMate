package com.example.chat_v1.client;

import com.example.chat_v1.dto.user.UserProfileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "authentication-service")
public interface IChatClient {
    @GetMapping("/api/profiles/search/findMultiple")
    ResponseEntity<List<UserProfileResponseDTO>> findMulitpleUsers(@RequestParam("userId") List<Long> userIds,
                                                                   @RequestHeader("Authorization") String bearerToken);
}
