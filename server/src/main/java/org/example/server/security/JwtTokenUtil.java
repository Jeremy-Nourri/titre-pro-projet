package org.example.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.server.exception.TokenExpiredException;
import org.example.server.exception.TokenInvalidException;
import org.example.server.repository.TokenBlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    private final SecretKey secretKey;
    private final long expirationTime;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.expiration}") long expirationTime,
                        TokenBlacklistRepository tokenBlacklistRepository) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("Le secret JWT doit comporter au moins 32 caractères");
        }
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.expirationTime = expirationTime;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    public String generateToken(String email, Long id) {
        log.info("Génération du token pour user: {}, id: {}", email, id);
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims validateToken(String token) throws TokenInvalidException, TokenExpiredException {
        try {
            log.info("Validation du token en cours");
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.warn("Token expiré", ex);
            throw new TokenExpiredException("Token expiré", ex);
        } catch (JwtException ex) {
            log.warn("La validation du token a échouée", ex);
            throw new TokenInvalidException("Token invalide", ex);
        }
    }

    public boolean isTokenValid(String token) {
        if (tokenBlacklistRepository.existsByToken(token)) {
            log.warn("Token blacklisté");
            throw new TokenInvalidException("Token blacklisté");
        }

        try {
            validateToken(token);
            log.info("Token valide");
            return true;
        } catch (TokenExpiredException | TokenInvalidException ex) {
            return false;
        }
    }
}
