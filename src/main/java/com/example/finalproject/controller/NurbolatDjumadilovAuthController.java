package com.example.finalproject.controller;

import com.example.finalproject.dto.request.LoginRequest;
import com.example.finalproject.dto.request.RegisterRequest;
import com.example.finalproject.dto.response.JwtResponse;
import com.example.finalproject.service.NurbolatDjumadilovAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Register and login endpoints")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class NurbolatDjumadilovAuthController {

    private final NurbolatDjumadilovAuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Login and receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
