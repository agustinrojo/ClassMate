package com.example.api_gateway.clients;

import com.example.api_gateway.dto.TokenValidationRequest;
import com.example.api_gateway.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTHENTICATION-SERVICE")
public interface AuthenticationServiceClient {
    @PostMapping("/validate-token")
    ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest tokenValidationRequest);
}
