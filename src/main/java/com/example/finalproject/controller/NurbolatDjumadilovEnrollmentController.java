package com.example.finalproject.controller;

import com.example.finalproject.dto.response.EnrollmentResponse;
import com.example.finalproject.service.NurbolatDjumadilovEnrollmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollments", description = "Student course enrollment")
@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class NurbolatDjumadilovEnrollmentController {

    private final NurbolatDjumadilovEnrollmentService enrollmentService;

    @io.swagger.v3.oas.annotations.Operation(summary = "Enroll in a course")
    @PostMapping("/course/{courseId}")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long courseId,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.enroll(courseId, userDetails.getUsername()));
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Get my enrollments")
    @GetMapping("/my")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(userDetails.getUsername()));
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Unenroll from a course")
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Void> unenroll(@PathVariable Long courseId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        enrollmentService.unenroll(courseId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
