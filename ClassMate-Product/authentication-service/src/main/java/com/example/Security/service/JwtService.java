package com.example.Security.service;

import com.example.Security.entities.JWTToken;
import com.example.Security.entities.User;
import com.example.Security.repositories.JWTTokenrepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;
    private final JWTTokenrepository jwtTokenrepository;

    public JwtService(JWTTokenrepository jwtTokenrepository) {
        this.jwtTokenrepository = jwtTokenrepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    public String generateToken(UserDetails userDetails){
//        return generateAccessToken(new HashMap<>(), userDetails);
//    }

    public String generateAccessToken(User userDetails){

        return generateToken(userDetails,  accessTokenExpire);
    }

    public String generateRefreshToken(User userDetails){

        return generateToken(userDetails, refreshTokenExpire);
    }

    public boolean isUserTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);

        boolean isLoggedOut = jwtTokenrepository.findByToken(token)
                                .map(JWTToken::isLoggedOut)
                                .orElse(true);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !isLoggedOut;
    }

    public boolean validateToken(String token){
        boolean isLoggedOut = jwtTokenrepository.findByToken(token)
                .map(JWTToken::isLoggedOut)
                .orElse(true);

        return !isLoggedOut && !isTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String token, User user) {
        final String username = extractUsername(token);


        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        //new Date() ->  fecha de hoy
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) //signInKey es un secret que se usa para firmar digitalmente el JWT. Se usa para crear la parte de la firma del JWT
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String  generateToken(@NotNull User user, long expirationTime){
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("role", user.getRole());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }



}
