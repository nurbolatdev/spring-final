package com.example.finalproject.controller;

import com.example.finalproject.dto.request.CourseRequest;
import com.example.finalproject.dto.response.CourseResponse;
import com.example.finalproject.service.NurbolatDjumadilovCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "Course management")
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class NurbolatDjumadilovCourseController {

    private final NurbolatDjumadilovCourseService courseService;

    @Operation(summary = "Get all courses with optional search and category filter")
    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(courseService.getAll(search, categoryId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<CourseResponse>> getMyCourses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(courseService.getByTeacherEmail(userDetails.getUsername()));
    }

    @Operation(summary = "Get courses by teacher username")
    @GetMapping("/teacher/{username}")
    public ResponseEntity<List<CourseResponse>> getByTeacher(@PathVariable String username) {
        return ResponseEntity.ok(courseService.getByTeacherUsername(username));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.create(request, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER','ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
