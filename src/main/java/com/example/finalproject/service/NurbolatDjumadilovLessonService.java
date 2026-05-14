package com.example.finalproject.service;

import com.example.finalproject.dto.request.LessonRequest;
import com.example.finalproject.dto.response.LessonResponse;
import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.Lesson;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.mapper.NurbolatDjumadilovLessonMapper;
import com.example.finalproject.repository.CourseRepository;
import com.example.finalproject.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovLessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final NurbolatDjumadilovLessonMapper lessonMapper;

    public List<LessonResponse> getByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndex(courseId)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    public LessonResponse getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        return lessonMapper.toResponse(lesson);
    }

    @Transactional
    public LessonResponse create(LessonRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setCourse(course);

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public LessonResponse update(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setOrderIndex(request.getOrderIndex());

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public void delete(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        lessonRepository.delete(lesson);
    }
}
