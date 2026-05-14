package com.example.finalproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseStatsResponse {

    private Long courseId;
    private String courseTitle;
    private int enrollmentCount;
    private int lessonCount;
    private double averageRating;
    private int reviewCount;
}
