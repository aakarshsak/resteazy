package com.sinha.resteazy.services;

import com.sinha.resteazy.daos.TokenRepository;
import com.sinha.resteazy.entities.Token;
import com.sinha.resteazy.exceptions.TokenNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private JwtService jwtService;

    @Autowired
    public LogoutService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        Token storedToken = jwtService.getToken(authHeader.substring(7));
        System.out.println("Pahucha kya..." + storedToken);
        if(storedToken == null) {
            throw new TokenNotFoundException("Invalid credentials");
        }
        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        jwtService.saveToken(storedToken);
        SecurityContextHolder.clearContext();

    }
}
