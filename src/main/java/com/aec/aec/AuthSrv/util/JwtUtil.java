package com.aec.aec.AuthSrv.util;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import java.util.Map;        //  ←  IMPORT
import java.util.HashMap;   //  ←  IMPORT

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretBase64;

    private Key hmacKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        this.hmacKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject, String role, long ttlMs) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + ttlMs))
                   .signWith(hmacKey, SignatureAlgorithm.HS256)
                   .compact();
    }

    /** Sobrecarga previa (sin rol) — por compatibilidad */
    public String generateToken(String subject, long ttlMs) {
        return generateToken(subject, "", ttlMs);
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(hmacKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                              .setSigningKey(hmacKey)
                              .build()
                              .parseClaimsJws(token)
                              .getBody()
                              .getExpiration();
        return expiration.before(new Date());
    }
}

