package com.sinha.resteazy.controllers;

import com.sinha.resteazy.entities.LoginRequestBody;
import com.sinha.resteazy.entities.RegisterRequestBody;
import com.sinha.resteazy.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequestBody requestBody) {
        return new ResponseEntity<>(authenticationService.register(requestBody), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestBody loginRequestBody) {
        return new ResponseEntity<>(authenticationService.login(loginRequestBody), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        authenticationService.refreshToken(req, res);
    }
}
