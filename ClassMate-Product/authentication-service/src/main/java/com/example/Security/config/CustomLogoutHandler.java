package com.example.Security.config;

import com.example.Security.entities.JWTToken;
import com.example.Security.repositories.JWTTokenrepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTTokenrepository jwtTokenrepository;

    public CustomLogoutHandler(JWTTokenrepository jwtTokenrepository) {
        this.jwtTokenrepository = jwtTokenrepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        String token = authHeader.substring(7);
        //buscar este token en bd
        JWTToken storedToken = jwtTokenrepository.findByToken(token)
                .orElse(null);
        //invalidar el token
        if(storedToken != null){
            storedToken.setLoggedOut(true);
            //guardar el token
            jwtTokenrepository.save(storedToken);
        }


    }
}
