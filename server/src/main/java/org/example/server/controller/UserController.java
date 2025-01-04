package org.example.server.controller;

import jakarta.validation.Valid;
import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDtoRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
