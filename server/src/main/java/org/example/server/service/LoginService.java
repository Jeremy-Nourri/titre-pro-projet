package org.example.server.service;

import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;

public interface LoginService {
    LoginDtoResponse login(LoginDtoRequest loginDtoRequest);

    Boolean logout(String token);

}
