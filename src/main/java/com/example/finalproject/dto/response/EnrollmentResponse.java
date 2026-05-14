package com.example.finalproject.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentResponse {

    private Long id;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseTitle;
    private LocalDate enrolledAt;
}
