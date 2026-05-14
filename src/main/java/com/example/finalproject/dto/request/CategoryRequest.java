package com.example.finalproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}
