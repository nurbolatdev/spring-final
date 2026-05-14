package com.example.finalproject.controller;

import com.example.finalproject.dto.request.LessonRequest;
import com.example.finalproject.dto.response.LessonResponse;
import com.example.finalproject.service.NurbolatDjumadilovLessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class NurbolatDjumadilovLessonController {

    private final NurbolatDjumadilovLessonService lessonService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonResponse>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getByCourse(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
