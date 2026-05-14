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
import com.example.finalproject.repository.LessonRepository;
import com.example.finalproject.repository.ReviewRepository;
import com.example.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NurbolatDjumadilovCourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private NurbolatDjumadilovCourseMapper courseMapper;

    @InjectMocks
    private NurbolatDjumadilovCourseService courseService;

    @Test
    void getById_shouldThrow_whenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void create_shouldReturnCourseResponse_whenValid() {
        CourseRequest request = new CourseRequest();
        request.setTitle("Java Basics");
        request.setDescription("Learn Java");
        request.setCategoryId(1L);

        User teacher = new User();
        teacher.setId(1L);
        teacher.setEmail("teacher@test.com");

        Category category = new Category();
        category.setId(1L);
        category.setTitle("Programming");

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setTitle("Java Basics");
        savedCourse.setTeacher(teacher);
        savedCourse.setCategory(category);

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setId(1L);
        expectedResponse.setTitle("Java Basics");

        when(userRepository.findByEmail("teacher@test.com")).thenReturn(Optional.of(teacher));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        when(courseMapper.toResponse(savedCourse)).thenReturn(expectedResponse);

        CourseResponse response = courseService.create(request, "teacher@test.com");

        assertThat(response.getTitle()).isEqualTo("Java Basics");
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void delete_shouldThrow_whenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
