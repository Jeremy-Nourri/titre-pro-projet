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
            log.info("Processing request to: {}", request.getRequestURI());

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
                    log.warn("Invalid token");
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid token");
                    return;
                }
            } else {
                log.warn("Authorization header is missing or does not start with Bearer");
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.error("Token has expired", ex);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
        } catch (JwtException ex) {
            log.error("Invalid token", ex);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid token");
        } catch (Exception ex) {
            log.error("An unexpected error occurred", ex);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
