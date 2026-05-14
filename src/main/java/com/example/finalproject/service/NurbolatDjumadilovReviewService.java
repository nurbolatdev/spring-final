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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final NurbolatDjumadilovReviewMapper reviewMapper;

    public List<ReviewResponse> getByCourse(Long courseId) {
        return reviewRepository.findByCourseId(courseId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    public List<ReviewResponse> getByStudent(Long studentId) {
        return reviewRepository.findByStudentId(studentId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    public Double getAverageRating(Long courseId) {
        Double avg = reviewRepository.findAverageRatingByCourseId(courseId);
        return avg != null ? avg : 0.0;
    }

    @Transactional
    public ReviewResponse create(ReviewRequest request, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (reviewRepository.existsByCourseIdAndStudentId(course.getId(), student.getId())) {
            throw new IllegalStateException("You have already reviewed this course");
        }

        Review review = new Review();
        review.setCourse(course);
        review.setStudent(student);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Transactional
    public ReviewResponse update(Long reviewId, ReviewRequest request, String studentEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getStudent().getEmail().equals(studentEmail)) {
            throw new IllegalStateException("You can only edit your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Transactional
    public void delete(Long reviewId, String studentEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getStudent().getEmail().equals(studentEmail)) {
            throw new IllegalStateException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }
}
