package com.example.Security.controllers;

import com.example.Security.dto.auth.AuthReq;
import com.example.Security.dto.auth.AuthenticationResp;
import com.example.Security.dto.register.RegisterReq;
import com.example.Security.dto.register.RegisterRespDTO;
import com.example.Security.dto.token.TokenValidationRequest;
import com.example.Security.dto.token.TokenValidationResponse;
import com.example.Security.dto.token.UserTokenValidationRequest;
import com.example.Security.service.AuthService;
import com.example.Security.service.ConfirmationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationControllers {

    private final AuthService authService;
    private final ConfirmationTokenService confirmationTokenService;


    @PostMapping("/register")
    public ResponseEntity<RegisterRespDTO> register(@RequestBody RegisterReq request){
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token){
        return new ResponseEntity<String>(confirmationTokenService.confirmToken(token), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResp> authenticate(@RequestBody AuthReq request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/validate-user-token")
    public ResponseEntity<TokenValidationResponse> validate(@RequestBody UserTokenValidationRequest request){
        return new ResponseEntity<TokenValidationResponse>(authService.isUserTokenValid(request), HttpStatus.OK);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validate(@RequestBody TokenValidationRequest request){
        return new ResponseEntity<TokenValidationResponse>(authService.validateToken(request), HttpStatus.OK);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response){
        return authService.refreshToken(request, response);
    }
}
