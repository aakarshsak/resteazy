package com.sinha.resteazy.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinha.resteazy.controllers.TokenResponse;
import com.sinha.resteazy.entities.*;
import com.sinha.resteazy.services.JwtService;
import com.sinha.resteazy.services.user.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationService {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UserDetailsService userDetailsService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(PasswordEncoder encoder, UserService userService, JwtService jwtService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = encoder;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse register(RegisterRequestBody requestBody) {
        User user = User.builder()
                .email(requestBody.getEmail())
                .password(passwordEncoder.encode(requestBody.getPassword()))
                .phone(requestBody.getPhone())
                .address(requestBody.getAddress())
                .firstName(requestBody.getFirstName())
                .lastName(requestBody.getLastName())
                .gender(requestBody.getGender())
                .role(requestBody.getRole())
                .build();

        userService.addNewUser(user);
        String token = jwtService.generateToken(user);
        jwtService.revokeUserTokens(user.getEmail());
        jwtService.saveToken(token, user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user);
        return TokenResponse.builder().accessToken(token).refreshToken(refreshToken).build();
    }

    public TokenResponse login(LoginRequestBody request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        jwtService.revokeUserTokens(user.getUsername());
        jwtService.saveToken(token, user.getUsername());

        String refreshToken = jwtService.generateRefreshToken(user);

        return TokenResponse.builder().accessToken(token).refreshToken(refreshToken).build();
    }

    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);
            if(jwtService.isValidToken(refreshToken, userDetails)) {
                String accessToken = jwtService.generateToken(userDetails);
                TokenResponse tokenResponse = TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                jwtService.revokeUserTokens(userEmail);
                jwtService.saveToken(accessToken, userEmail);
                new ObjectMapper().writeValue(res.getOutputStream(), tokenResponse);
            }
        }

    }
}
