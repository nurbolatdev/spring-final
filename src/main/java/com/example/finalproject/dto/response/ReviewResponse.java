package com.example.finalproject.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private Long studentId;
    private String studentUsername;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
