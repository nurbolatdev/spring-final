package com.example.finalproject.controller;

import com.example.finalproject.dto.response.ProgressResponse;
import com.example.finalproject.service.NurbolatDjumadilovProgressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Progress", description = "Student course progress tracking")
@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class NurbolatDjumadilovProgressController {

    private final NurbolatDjumadilovProgressService progressService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable Long courseId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.getProgress(courseId, userDetails.getUsername()));
    }

    @PutMapping("/course/{courseId}")
    public ResponseEntity<ProgressResponse> updateProgress(@PathVariable Long courseId,
                                                            @RequestParam int completedLessons,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.updateProgress(courseId, userDetails.getUsername(), completedLessons));
    }
}
