package com.example.calendar_service.controller;


import com.example.calendar_service.service.IGoogleOAuth2Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${google.client-id}")
    private String clientId;


    private final IGoogleOAuth2Service googleOAuth2Service;

    public OAuth2Controller(IGoogleOAuth2Service googleOAuth2Service) {
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @GetMapping("/connect/google")
    public void connectGoogle(@RequestParam("userId") Long userId, HttpServletResponse response) throws IOException {
        // Codificar el userId en el parámetro state
        String state = URLEncoder.encode(String.format("userId=%d", userId), "UTF-8");
        String googleOAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.events&" +
                "access_type=offline&" +
                "include_granted_scopes=true&" +
                "response_type=code&" +
                "state=" + state + "&" + // Añadir el parámetro state con userId
                "redirect_uri=http://localhost:8085/oauth2/callback/google&" +
                String.format("client_id=%s", clientId);
        response.sendRedirect(googleOAuthUrl);
    }

    @GetMapping("/callback/google")
    public void googleCallback(@RequestParam("code") String code,
                               @RequestParam("state") String state,
                               HttpServletResponse response) throws IOException {
        // Extraer el userId del parámetro state
        String userIdParam = URLDecoder.decode(state, "UTF-8");

        Long userId = Long.parseLong(userIdParam.split("=")[1]); // Obtener el userId
        System.out.println(userId);
        googleOAuth2Service.getAccessToken(code, userId);

        response.sendRedirect("http://localhost:4200/calendar"); // Redirige al usuario a la vista del calendario
    }
}
