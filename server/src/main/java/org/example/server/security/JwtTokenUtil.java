package org.example.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    public String generateToken(String email, Long id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
