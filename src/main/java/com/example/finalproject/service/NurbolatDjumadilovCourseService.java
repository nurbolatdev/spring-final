package com.example.finalproject.service;

import com.example.finalproject.dto.request.CourseRequest;
import com.example.finalproject.dto.response.CourseResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.mapper.NurbolatDjumadilovCourseMapper;
import com.example.finalproject.repository.CategoryRepository;
import com.example.finalproject.repository.CourseRepository;
import com.example.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovCourseService {

    private static final Logger log = LoggerFactory.getLogger(NurbolatDjumadilovCourseService.class);

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NurbolatDjumadilovCourseMapper courseMapper;

    public Page<CourseResponse> getAll(String search, Long categoryId, Pageable pageable) {
        return courseRepository.findWithFilters(search, categoryId, pageable)
                .map(courseMapper::toResponse);
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return courseMapper.toResponse(course);
    }

    public List<CourseResponse> getByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public List<CourseResponse> getByTeacherEmail(String email) {
        User teacher = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return courseRepository.findByTeacherId(teacher.getId())
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public List<CourseResponse> getByTeacherUsername(String username) {
        return courseRepository.findByTeacherUsername(username)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse create(CourseRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(category);
        course.setTeacher(teacher);

        log.info("Creating course: {} by teacher: {}", request.getTitle(), teacherEmail);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(category);

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        log.info("Deleting course: {}", course.getTitle());
        courseRepository.delete(course);
    }
}
