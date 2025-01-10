package org.example.server.security;

import org.example.server.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/login", "/api/users/register").permitAll()
                        .anyRequest().authenticated()

                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173" ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        configuration.setExposedHeaders(List.of("Authorization"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        log.info("Création du JwtAuthenticationFilter bean");

        return new JwtAuthenticationFilter(jwtTokenUtil, userRepository);
    }
}