package com.example.finalproject.service;

import com.example.finalproject.dto.response.EnrollmentResponse;
import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.Enrollment;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.repository.CourseRepository;
import com.example.finalproject.repository.EnrollmentRepository;
import com.example.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NurbolatDjumadilovEnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private NurbolatDjumadilovEmailService emailService;

    @InjectMocks
    private NurbolatDjumadilovEnrollmentService enrollmentService;

    @Test
    void enroll_shouldThrow_whenAlreadyEnrolled() {
        User user = new User();
        user.setId(1L);
        user.setEmail("student@test.com");

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java Basics");

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByUserIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll(1L, "student@test.com"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Already enrolled");
    }

    @Test
    void enroll_shouldReturnResponse_whenValid() {
        User user = new User();
        user.setId(1L);
        user.setEmail("student@test.com");
        user.setUsername("student");

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java Basics");

        Enrollment saved = new Enrollment();
        saved.setId(1L);
        saved.setUser(user);
        saved.setCourse(course);

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByUserIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);
        when(emailService.sendEnrollmentConfirmation(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        EnrollmentResponse response = enrollmentService.enroll(1L, "student@test.com");

        assertThat(response.getCourseTitle()).isEqualTo("Java Basics");
        assertThat(response.getUsername()).isEqualTo("student");
    }
}
