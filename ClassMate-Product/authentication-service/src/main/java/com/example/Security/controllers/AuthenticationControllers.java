package com.example.Security.controllers;

import com.example.Security.dto.AuthReq;
import com.example.Security.dto.AuthenticationResp;
import com.example.Security.dto.RegisterReq;
import com.example.Security.service.AuthService;
import com.example.Security.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationControllers {

    private final AuthService authService;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterReq request){
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
}
