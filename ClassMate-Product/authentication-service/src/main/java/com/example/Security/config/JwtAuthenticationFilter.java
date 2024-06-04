package com.example.Security.config;

import com.example.Security.exception.ErrorDetails;
import com.example.Security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()
            .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, //sirve para interceptar la req y extraer datos
            @NonNull HttpServletResponse response, //sirve para controlar la resp
            @NonNull FilterChain filterChain //lista de filtros (chain of responsibility)
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Este es el header que contiene el Bearer Token(JWT)
        final String jwt;
        final String userEmail;

        String path =  request.getRequestURL().toString();



        // para filtrar headers vacios o no validos
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response); // se le pasa la req y la resp al proximo filtro
            return;
        }
        jwt = authHeader.substring(7);
        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isUserTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
//                    setErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, request, "Invalid JWT token", "INVALID_TOKEN");
//                    return;
                }
            }

        } catch (ExpiredJwtException e) {
            setErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, request, "JWT token has expired", "TOKEN_EXPIRED");
            return;
        } catch (MalformedJwtException | SignatureException e) {
//            setErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, request, "Invalid JWT token", "INVALID_TOKEN");
            return;
        } catch (Exception e) {
            setErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, request, "An error occurred while processing the JWT token", "INTERNAL_ERROR");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(int status, HttpServletResponse response, HttpServletRequest request, String message, String errorCode) throws IOException {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(message)
                .path(request.getRequestURI())
                .errorCode(errorCode)
                .build();
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
    
}
