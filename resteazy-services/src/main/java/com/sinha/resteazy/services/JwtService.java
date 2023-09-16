package com.sinha.resteazy.services;

import com.sinha.resteazy.daos.TokenRepository;
import com.sinha.resteazy.entities.Token;
import com.sinha.resteazy.entities.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.signInKey}")
    private String signInkey;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    @Value("${application.security.jwt.refreshToken.expiration}")
    private long refreshExpiration;

    private TokenRepository tokenRepository;

    @Autowired
    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        if(isExpiredToken(token)) {
            setTokenExpired(token);
        }
        return extractUsername(token).equals(userDetails.getUsername()) && !isExpiredToken(token);
    }

    private void setTokenExpired(String token) {
        Token tokenData = tokenRepository.findById(token).orElse(null);
        if(tokenData != null) {
            tokenData.setExpired(true);
        }
        tokenRepository.save(tokenData);
    }

    private boolean isExpiredToken(String token) {
        return getExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date getExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.signInkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return buildToken(userDetails, extraClaims, expiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, new HashMap<>(), refreshExpiration);
    }

    private String buildToken(UserDetails userDetails, Map<String, Object> extraClaims, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void revokeUserTokens(String userEmail) {
        List<Token> allTokens = tokenRepository.findAllByUserEmail(userEmail);
        if(allTokens.isEmpty()) return;
        allTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(allTokens);
    }

    public void saveToken(String token, String userEmail) {
        Token token1 = Token.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .userEmail(userEmail)
                .build();
        saveToken(token1);
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Token getToken(String token) {
        Optional<Token> tokenData = tokenRepository.findById(token);
        if(!tokenData.isPresent()) return null;
        return tokenData.get();
    }
}
