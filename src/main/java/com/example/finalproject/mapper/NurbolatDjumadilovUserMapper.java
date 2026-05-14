package com.example.finalproject.mapper;

import com.example.finalproject.dto.response.UserResponse;
import com.example.finalproject.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NurbolatDjumadilovUserMapper {

    UserResponse toResponse(User user);
}
