package com.example.calendar_service.service;

public interface IGoogleOAuth2Service {
    void getAccessToken(String code, Long userId);
    String getValidAccessToken(Long userId);
}
