package com.sinha.resteazy.services;


import com.sinha.resteazy.controllers.TokenResponse;
import com.sinha.resteazy.entities.LoginRequestBody;
import com.sinha.resteazy.entities.RegisterRequestBody;
import com.sinha.resteazy.entities.Role;
import com.sinha.resteazy.entities.User;
import com.sinha.resteazy.services.JwtService;
import com.sinha.resteazy.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return TokenResponse.builder().token(token).build();
    }

    public TokenResponse login(LoginRequestBody request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        return TokenResponse.builder().token(token).build();
    }
}
