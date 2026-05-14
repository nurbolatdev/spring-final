package com.example.finalproject.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private String categoryTitle;
    private String teacherUsername;
    private LocalDateTime createdAt;
}
