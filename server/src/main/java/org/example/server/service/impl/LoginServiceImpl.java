package org.example.server.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.exception.InvalidCredentialsException;
import org.example.server.mapper.ProjectMapper;
import org.example.server.mapper.UserProjectMapper;
import org.example.server.model.TokenBlacklist;
import org.example.server.model.User;
import org.example.server.repository.TokenBlacklistRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.LoginService;
import org.example.server.security.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public LoginDtoResponse login(LoginDtoRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe invalide"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email ou mot de passe invalide");
        }

        LoginDtoResponse response = new LoginDtoResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPosition(String.valueOf(user.getPosition()));
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());

        if (user.getCreatedProjects() != null) {
            response.setCreatedProjects(user.getCreatedProjects().stream()
                    .map(ProjectMapper::ProjectToCreatedProjectsDtoResponse)
                    .collect(Collectors.toList()));
        }

        if (user.getUserProjects() != null) {
            response.setUserProjects(user.getUserProjects().stream()
                    .map(UserProjectMapper::mapUserProjectToUserProjectDtoResponse)
                    .collect(Collectors.toList()));
        }

        response.setToken(jwtTokenUtil.generateToken(user.getEmail(), user.getId()));

        return response;
    }

    @Override
    public Boolean logout(String token) {
        Claims claims = jwtTokenUtil.validateToken(token);
        Date expiration = claims.getExpiration();

        TokenBlacklist blacklist = new TokenBlacklist();
        blacklist.setToken(token);
        blacklist.setExpiryDate(expiration);

        tokenBlacklistRepository.save(blacklist);
        return true;
    }

}
