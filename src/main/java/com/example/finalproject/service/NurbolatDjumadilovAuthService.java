package com.example.finalproject.service;

import com.example.finalproject.dto.request.LoginRequest;
import com.example.finalproject.dto.request.RegisterRequest;
import com.example.finalproject.dto.response.JwtResponse;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.repository.UserRepository;
import com.example.finalproject.security.NurbolatDjumadilovJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NurbolatDjumadilovJwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final NurbolatDjumadilovEmailService emailService;

    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.ROLE_STUDENT);

        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());

        String token = jwtUtil.generateToken(user.getEmail());
        return new JwtResponse(token, user.getId(), user.getUsername(), user.getRole().name());
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new JwtResponse(token, user.getId(), user.getUsername(), user.getRole().name());
    }
}
