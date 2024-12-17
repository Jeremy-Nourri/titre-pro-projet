package org.example.server.service.impl;

import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.exception.EmailExistsException;
import org.example.server.mapper.UserMapper;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDtoResponse createUser(UserDtoRequest userRequest) {

        Optional<User> userFound = userRepository.findByEmail(userRequest.getEmail());

        if (userFound.isPresent()) {
            throw new EmailExistsException();
        }

        User user = UserMapper.mapUserDtoRequestToUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.mapUserToUserDtoResponse(savedUser);
    }

    @Override
    public UserDtoResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.mapUserToUserDtoResponse(user);
    }
}
