package org.example.server.controller;

import jakarta.validation.Valid;
import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginDtoResponse> login(@Valid @RequestBody LoginDtoRequest loginDtoRequest) {
        LoginDtoResponse loginDtoResponse = loginService.login(loginDtoRequest);
        return ResponseEntity.ok(loginDtoResponse);
    }
}
