package com.example.calendar_service.service;

import com.example.calendar_service.entity.auth.Token;

public interface ITokenService {
    void saveToken(String accessToken, String refreshToken, Long expiresIn, Long userId);
    void saveToken(Token token);
    Token findTokenByUserId(Long userId);
    void deleteTokensByUserId(Long userId);
}
