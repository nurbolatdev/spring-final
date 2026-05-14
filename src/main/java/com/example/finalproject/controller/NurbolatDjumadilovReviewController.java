package com.example.finalproject.controller;

import com.example.finalproject.dto.request.ReviewRequest;
import com.example.finalproject.dto.response.ReviewResponse;
import com.example.finalproject.service.NurbolatDjumadilovReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reviews", description = "Course review management")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class NurbolatDjumadilovReviewController {

    private final NurbolatDjumadilovReviewService reviewService;

    @Operation(summary = "Get all reviews for a course")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ReviewResponse>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getByCourse(courseId));
    }

    @Operation(summary = "Get average rating for a course")
    @GetMapping("/course/{courseId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getAverageRating(courseId));
    }

    @Operation(summary = "Get all reviews by a student")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ReviewResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(reviewService.getByStudent(studentId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(request, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER','ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ReviewRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reviewService.update(id, request, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
