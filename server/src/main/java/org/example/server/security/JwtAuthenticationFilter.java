package org.example.server.security;

import org.example.server.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            String authorizationHeader = request.getHeader("Authorization");
            log.info("Traitement de la requête: {}", request.getRequestURI());

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);

                if (jwtTokenUtil.isTokenValid(token)) {
                    Claims claims = jwtTokenUtil.validateToken(token);
                    String email = claims.getSubject();
                    log.info("JWT valid, user: {}", email);

                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .authorities(Collections.emptyList())
                            .build();

                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, true);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.warn("Token non valide");
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid token");
                    return;
                }
            } else {
                log.warn("Authorization header est manquante ou ne commence pas par Bearer");
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.error("Token expiré", ex);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expiré");
        } catch (JwtException ex) {
            log.error("Token non valide", ex);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Token non valide");
        } catch (Exception ex) {
            log.error("Erreur inattendue", ex);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur inattendue");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"erreur\": \"" + message + "\"}");
    }
}
