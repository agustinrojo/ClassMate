package com.example.calendar_service.service.impl;

import com.example.calendar_service.entity.auth.Token;
import com.example.calendar_service.repository.ITokenRepository;
import com.example.calendar_service.service.ITokenService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements ITokenService {

    private final ITokenRepository tokenRepository;

    public TokenServiceImpl(ITokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void saveToken(String accessToken, String refreshToken, Long expiresIn, Long userId) {
        long currentTime = System.currentTimeMillis();

        if(tokenRepository.existsByUserId(userId)){
            Token existingToken = tokenRepository.findByUserId(userId).get(0);
            existingToken.setAccessToken(accessToken);
            existingToken.setExpirationTime(currentTime + expiresIn * 1000);
            if(refreshToken != null) existingToken.setRefreshToken(refreshToken);

            tokenRepository.save(existingToken);
        } else {
            Token newToken = Token.builder()
                    .accessToken(accessToken)
                    .expirationTime(currentTime + expiresIn * 1000)
                    .userId(userId)
                    .build();
            if(refreshToken != null) newToken.setRefreshToken(refreshToken);

            tokenRepository.save(newToken);
        }
    }

    @Override
    @Transactional
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public Token findTokenByUserId(Long userId) {
        return tokenRepository.findByUserId(userId).get(0);
    }


}
