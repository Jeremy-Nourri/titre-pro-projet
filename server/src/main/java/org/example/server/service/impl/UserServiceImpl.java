package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.exception.EmailExistsException;
import org.example.server.exception.UserNotFoundException;
import org.example.server.mapper.UserMapper;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userRequest) {
        if (userRequest == null) {
            throw new IllegalArgumentException("UserDtoRequest cannot be null");
        }

        Optional<User> userFound = userRepository.findByEmail(userRequest.getEmail());

        if (userFound.isPresent()) {
            throw new EmailExistsException("Email déjà utilisé");
        }

        User user = userMapper.mapUserDtoRequestToUser(userRequest);
        if (user == null) {
            throw new IllegalStateException("UserMapper returned null");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.mapUserToUserDtoResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.mapUserToUserDtoResponse(user);
    }
}
