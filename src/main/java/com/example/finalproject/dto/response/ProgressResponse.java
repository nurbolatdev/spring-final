package com.example.finalproject.dto.response;

import lombok.Data;

@Data
public class ProgressResponse {

    private Long id;
    private Long userId;
    private Long courseId;
    private String courseTitle;
    private Integer completedLessons;
    private Integer totalLessons;
    private Double percentage;
}
