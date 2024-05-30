package com.example.Security.config;

import com.example.Security.service.JwtService;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
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
        userEmail = jwtService.extractUsername(jwt);
        //chequea si el username(email) no es null y que el usuario no este ya logueado
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // se debe chequear el usuario contra la BD
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    
}
