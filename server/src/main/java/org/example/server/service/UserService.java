package org.example.server.service;

import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;

public interface UserService {
    UserDtoResponse createUser(UserDtoRequest userRequest);
    UserDtoResponse getUserById(Long id);
}
