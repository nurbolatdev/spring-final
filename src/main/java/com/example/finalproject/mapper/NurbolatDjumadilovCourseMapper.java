package com.example.finalproject.mapper;

import com.example.finalproject.dto.response.CourseResponse;
import com.example.finalproject.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NurbolatDjumadilovCourseMapper {

    @Mapping(source = "category.title", target = "categoryTitle")
    @Mapping(source = "teacher.username", target = "teacherUsername")
    @Mapping(expression = "java(course.getEnrollments() == null ? 0 : course.getEnrollments().size())", target = "studentCount")
    CourseResponse toResponse(Course course);
}
