package com.example.finalproject.mapper;

import com.example.finalproject.dto.response.ReviewResponse;
import com.example.finalproject.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NurbolatDjumadilovReviewMapper {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.username", target = "studentUsername")
    ReviewResponse toResponse(Review review);
}
