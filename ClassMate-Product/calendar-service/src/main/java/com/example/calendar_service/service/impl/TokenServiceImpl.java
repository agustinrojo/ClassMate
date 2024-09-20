package com.example.calendar_service.service.impl;

import com.example.calendar_service.entity.auth.Token;
import com.example.calendar_service.repository.ITokenRepository;
import com.example.calendar_service.service.ITokenService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenServiceImpl implements ITokenService {

    private final ITokenRepository tokenRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

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

    @Transactional
    @Override
    public void deleteTokensByUserId(Long userId){
        LOGGER.info(String.format("Deleting user %d Token", userId));
        tokenRepository.deleteAllByUserId(userId);
    }

}
