package com.example.finalproject.service;

import com.example.finalproject.dto.response.ProgressResponse;
import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.Progress;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.repository.CourseRepository;
import com.example.finalproject.repository.LessonRepository;
import com.example.finalproject.repository.ProgressRepository;
import com.example.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public ProgressResponse getProgress(Long courseId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Progress progress = progressRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseGet(() -> createProgress(user, courseId));

        return toResponse(progress);
    }

    public ProgressResponse updateProgress(Long courseId, String userEmail, int completedLessons) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Progress progress = progressRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseGet(() -> createProgress(user, courseId));

        int total = lessonRepository.countByCourseId(courseId);
        progress.setCompletedLessons(completedLessons);
        progress.setTotalLessons(total);
        progress.setPercentage(total > 0 ? (completedLessons * 100.0 / total) : 0.0);

        return toResponse(progressRepository.save(progress));
    }

    private Progress createProgress(User user, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Progress progress = new Progress();
        progress.setUser(user);
        progress.setCourse(course);
        progress.setCompletedLessons(0);
        progress.setTotalLessons(lessonRepository.countByCourseId(courseId));
        progress.setPercentage(0.0);
        return progressRepository.save(progress);
    }

    private ProgressResponse toResponse(Progress p) {
        ProgressResponse response = new ProgressResponse();
        response.setId(p.getId());
        response.setUserId(p.getUser().getId());
        response.setCourseId(p.getCourse().getId());
        response.setCourseTitle(p.getCourse().getTitle());
        response.setCompletedLessons(p.getCompletedLessons());
        response.setTotalLessons(p.getTotalLessons());
        response.setPercentage(p.getPercentage());
        return response;
    }
}
