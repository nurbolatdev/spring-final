package com.example.finalproject.service;

import com.example.finalproject.dto.response.EnrollmentResponse;
import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.Enrollment;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.repository.CourseRepository;
import com.example.finalproject.repository.EnrollmentRepository;
import com.example.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovEnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(NurbolatDjumadilovEnrollmentService.class);

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public EnrollmentResponse enroll(Long courseId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new BadRequestException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);

        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("User {} enrolled in course {}", userEmail, course.getTitle());

        return toResponse(saved);
    }

    public List<EnrollmentResponse> getMyEnrollments(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return enrollmentRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void unenroll(Long courseId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollmentRepository.delete(enrollment);
        log.info("User {} unenrolled from course {}", userEmail, courseId);
    }

    private EnrollmentResponse toResponse(Enrollment e) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(e.getId());
        response.setUserId(e.getUser().getId());
        response.setUsername(e.getUser().getUsername());
        response.setCourseId(e.getCourse().getId());
        response.setCourseTitle(e.getCourse().getTitle());
        response.setEnrolledAt(e.getEnrolledAt());
        return response;
    }
}
