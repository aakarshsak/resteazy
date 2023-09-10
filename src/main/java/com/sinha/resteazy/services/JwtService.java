package com.sinha.resteazy.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String signInkey = "20e052844e7b2eda8c4780d054a5de37cf112a124c42b75abea1dc3a9ce29180";

    public boolean isValidToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isExpiredToken(token);
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

    private String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
