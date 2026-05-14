package com.example.finalproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    private Integer orderIndex;

    @NotNull(message = "Course is required")
    private Long courseId;
}
