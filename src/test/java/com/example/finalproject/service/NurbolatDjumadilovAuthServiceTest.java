package com.example.finalproject.service;

import com.example.finalproject.dto.request.LoginRequest;
import com.example.finalproject.dto.request.RegisterRequest;
import com.example.finalproject.dto.response.JwtResponse;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.repository.UserRepository;
import com.example.finalproject.security.NurbolatDjumadilovJwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NurbolatDjumadilovAuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NurbolatDjumadilovJwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private NurbolatDjumadilovEmailService emailService;

    @InjectMocks
    private NurbolatDjumadilovAuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
    }

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void register_shouldReturnToken_whenSuccessful() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtUtil.generateToken(any())).thenReturn("jwt-token");
        when(emailService.sendWelcomeEmail(any(), any())).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(null));

        JwtResponse response = authService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getUsername()).isEqualTo("testuser");
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setRole(User.Role.ROLE_STUDENT);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("test@example.com")).thenReturn("jwt-token");

        JwtResponse response = authService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getRole()).isEqualTo("ROLE_STUDENT");
    }
}
