package org.example.server.controller;

import jakarta.validation.Valid;
import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDtoResponse> login(@Valid @RequestBody LoginDtoRequest loginDtoRequest) {
        LoginDtoResponse loginDtoResponse = loginService.login(loginDtoRequest);
        return ResponseEntity.ok(loginDtoResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean result = loginService.logout(token);
            if (result) {
                SecurityContextHolder.clearContext();
                return ResponseEntity.ok().body("Déconnexion réussie");
            } else {
                return ResponseEntity.status(403).body("Token invalide ou blacklisté");
            }
        }
        return ResponseEntity.badRequest().body("Authorization header manquante ou invalide");
    }
}
