package com.example.calendar_service.service.impl;

import com.example.calendar_service.client.GoogleOAuth2APIClient;
import com.example.calendar_service.entity.auth.Token;
import com.example.calendar_service.publisher.SyncPublisher;
import com.example.calendar_service.service.IGoogleCalendarService;
import com.example.calendar_service.service.IGoogleOAuth2Service;
import com.example.calendar_service.service.ITokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleOAuth2ServiceImpl implements IGoogleOAuth2Service {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    private final GoogleOAuth2APIClient googleOAuth2APIClient;

    private final ITokenService tokenService;

    private final SyncPublisher syncPublisher;


    public GoogleOAuth2ServiceImpl(GoogleOAuth2APIClient googleOAuth2APIClient, ITokenService tokenService, SyncPublisher syncPublisher) {
        this.googleOAuth2APIClient = googleOAuth2APIClient;
        this.tokenService = tokenService;
        this.syncPublisher = syncPublisher;
    }

    @Override
    public void getAccessToken(String code, Long userId, boolean isSynced) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "http://localhost:8085/oauth2/callback/google");
        params.add("grant_type", "authorization_code");
        params.add("access_type", "offline");
        params.add("prompt", "consent");

        Map<String, String> formParams = new HashMap<>();
        formParams.putAll(params.toSingleValueMap());

        // Llamar a Google para obtener el token
        Map<String, String> tokenData = googleOAuth2APIClient.getAccessToken(formParams);

        // Obtener el access_token y refresh_token
        String accessToken = tokenData.get("access_token");
        String refreshToken = tokenData.get("refresh_token");
        Long expiresIn = Long.valueOf(tokenData.get("expires_in"));

        tokenService.saveToken(accessToken, refreshToken, expiresIn, userId);

        if(!isSynced){
            syncPublisher.publishSyncGoogle(userId, true);
        }

    }

    @Override
    public String getValidAccessToken(Long userId) {
        Token token = tokenService.findTokenByUserId(userId);

        long currentTime = System.currentTimeMillis();

        if(currentTime >= token.getExpirationTime()){
            Map<String, String> tokenData = refreshAccessToken(userId);
            String accessToken = tokenData.get("access_token");
            long expiresIn = Long.parseLong(tokenData.get("expires_in"));
            token.setAccessToken(accessToken);
            token.setExpirationTime(currentTime + expiresIn * 1000);
            tokenService.saveToken(token);
        }

        return token.getAccessToken();
    }


    public void unSyncronize(Long userId){
        tokenService.deleteTokensByUserId(userId);

        syncPublisher.publishSyncGoogle(userId, false);
    }

    private Map<String, String> refreshAccessToken(Long userId) {
        String refreshToken = tokenService.findTokenByUserId(userId).getRefreshToken();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");

        Map<String, String> formParams = new HashMap<>();
        formParams.putAll(params.toSingleValueMap());

        return googleOAuth2APIClient.getAccessToken(formParams);

    }


}
