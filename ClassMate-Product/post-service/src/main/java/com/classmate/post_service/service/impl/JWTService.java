package com.classmate.post_service.service.impl;

import com.classmate.post_service.entity.enums.Role;
import com.classmate.post_service.service.IJWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JWTService implements IJWTService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Role extractRole(String token) {
        String bearerToken = token.substring(7);
        String roleString = extractClaims(bearerToken).get("role", String.class);
        return Role.valueOf(roleString);
    }
}
