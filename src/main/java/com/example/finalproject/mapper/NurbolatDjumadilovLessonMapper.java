package com.example.finalproject.mapper;

import com.example.finalproject.dto.response.LessonResponse;
import com.example.finalproject.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NurbolatDjumadilovLessonMapper {

    @Mapping(source = "course.id", target = "courseId")
    LessonResponse toResponse(Lesson lesson);
}
