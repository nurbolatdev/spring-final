package com.example.finalproject.service;

import com.example.finalproject.dto.request.ReviewRequest;
import com.example.finalproject.dto.response.ReviewResponse;
import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.Review;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.mapper.NurbolatDjumadilovReviewMapper;
import com.example.finalproject.repository.CourseRepository;
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
class NurbolatDjumadilovReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NurbolatDjumadilovReviewMapper reviewMapper;

    @InjectMocks
    private NurbolatDjumadilovReviewService reviewService;

    @Test
    void create_shouldReturnReviewResponse_whenValid() {
        ReviewRequest request = new ReviewRequest();
        request.setCourseId(1L);
        request.setRating(5);
        request.setComment("Great course!");

        User student = new User();
        student.setId(1L);
        student.setEmail("student@test.com");

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java Basics");

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setRating(5);
        savedReview.setComment("Great course!");
        savedReview.setCourse(course);
        savedReview.setStudent(student);

        ReviewResponse expectedResponse = new ReviewResponse();
        expectedResponse.setId(1L);
        expectedResponse.setRating(5);

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(reviewRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        when(reviewMapper.toResponse(savedReview)).thenReturn(expectedResponse);

        ReviewResponse response = reviewService.create(request, "student@test.com");

        assertThat(response.getRating()).isEqualTo(5);
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void create_shouldThrow_whenAlreadyReviewed() {
        ReviewRequest request = new ReviewRequest();
        request.setCourseId(1L);
        request.setRating(4);

        User student = new User();
        student.setId(1L);
        student.setEmail("student@test.com");

        Course course = new Course();
        course.setId(1L);

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(reviewRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> reviewService.create(request, "student@test.com"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already reviewed");
    }

    @Test
    void create_shouldThrow_whenCourseNotFound() {
        ReviewRequest request = new ReviewRequest();
        request.setCourseId(99L);
        request.setRating(3);

        User student = new User();
        student.setId(1L);
        student.setEmail("student@test.com");

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.create(request, "student@test.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void delete_shouldThrow_whenReviewBelongsToAnotherUser() {
        User owner = new User();
        owner.setId(1L);
        owner.setEmail("owner@test.com");

        Review review = new Review();
        review.setId(1L);
        review.setStudent(owner);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> reviewService.delete(1L, "other@test.com"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("your own reviews");
    }
}
