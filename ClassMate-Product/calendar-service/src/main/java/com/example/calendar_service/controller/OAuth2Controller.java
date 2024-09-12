package com.example.calendar_service.controller;


import com.example.calendar_service.service.IEventService;
import com.example.calendar_service.service.IGoogleOAuth2Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @Value("${google.client-id}")
    private String clientId;


    private final IGoogleOAuth2Service googleOAuth2Service;

    private final IEventService eventService;

    public OAuth2Controller(IGoogleOAuth2Service googleOAuth2Service, IEventService eventService) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.eventService = eventService;
    }

    @GetMapping("/connect/google")
    public void connectGoogle(@RequestParam("userId") Long userId, @RequestParam("isSynced") boolean isSynced, HttpServletResponse response) throws IOException {
        // Formar el parámetro state con ambos valores codificados
        String state = String.format("userId=%d&isSynced=%b", userId, isSynced);

        // Construir la URL de autenticación de Google OAuth2
        String googleOAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.events&" +
                "access_type=offline&" +
                "include_granted_scopes=true&" +
                "response_type=code&" +
                "state=" + URLEncoder.encode(state, "UTF-8") + "&" +  // Codificar todo el estado para garantizar compatibilidad con URLs
                "redirect_uri=http://localhost:8085/oauth2/callback/google&" +
                String.format("client_id=%s", clientId) + "&" +
                "prompt=select_account";

        // Redirigir al usuario a la URL de Google OAuth2
        response.sendRedirect(googleOAuthUrl);
    }

    @GetMapping("/callback/google")
    public void googleCallback(@RequestParam("code") String code,
                               @RequestParam("state") String state,
                               HttpServletResponse response) throws IOException {
        String decodedState = URLDecoder.decode(state, "UTF-8");
        Map<String, String> stateParams = Arrays.stream(decodedState.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));

        // Extraer userId y isSynced del state
        Long userId = Long.valueOf(stateParams.get("userId"));  // Esto será "1"
        boolean isSynced = Boolean.parseBoolean(stateParams.get("isSynced"));
        googleOAuth2Service.getAccessToken(code, userId, isSynced);

        if(!isSynced){
            eventService.uploadAllEvents(userId, "primary");
        }

        response.sendRedirect("http://localhost:4200/calendar"); // Redirige al usuario a la vista del calendario
    }
}
