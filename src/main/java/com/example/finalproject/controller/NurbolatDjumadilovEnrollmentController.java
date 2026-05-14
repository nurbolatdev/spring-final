package com.example.finalproject.controller;

import com.example.finalproject.dto.response.EnrollmentResponse;
import com.example.finalproject.service.NurbolatDjumadilovEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class NurbolatDjumadilovEnrollmentController {

    private final NurbolatDjumadilovEnrollmentService enrollmentService;

    @PostMapping("/course/{courseId}")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long courseId,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.enroll(courseId, userDetails.getUsername()));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(userDetails.getUsername()));
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Void> unenroll(@PathVariable Long courseId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        enrollmentService.unenroll(courseId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
