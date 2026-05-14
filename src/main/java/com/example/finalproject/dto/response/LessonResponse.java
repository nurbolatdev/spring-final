package com.example.finalproject.dto.response;

import lombok.Data;

@Data
public class LessonResponse {

    private Long id;
    private String title;
    private String content;
    private Integer orderIndex;
    private Long courseId;
}
