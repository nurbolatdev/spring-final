package com.example.finalproject.service;

import com.example.finalproject.dto.request.ChangePasswordRequest;
import com.example.finalproject.dto.request.UpdateProfileRequest;
import com.example.finalproject.dto.response.UserResponse;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.mapper.NurbolatDjumadilovUserMapper;
import com.example.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovUserService {

    private final UserRepository userRepository;
    private final NurbolatDjumadilovUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (userRepository.existsByUsername(request.getUsername())
                && !user.getUsername().equals(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        user.setUsername(request.getUsername());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setRole(User.Role.valueOf(role));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
