package com.example.finalproject.service;

import com.example.finalproject.entity.Course;
import com.example.finalproject.entity.FileResource;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.repository.CourseRepository;
import com.example.finalproject.repository.FileResourceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovFileService {

    private static final Logger log = LoggerFactory.getLogger(NurbolatDjumadilovFileService.class);

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf", "video/mp4", "image/jpeg", "image/png",
            "application/zip", "text/plain"
    );

    private final FileResourceRepository fileResourceRepository;
    private final CourseRepository courseRepository;

    public FileResource uploadFile(MultipartFile file, Long courseId) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("File type not allowed: " + file.getContentType());
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            FileResource fileResource = new FileResource();
            fileResource.setFileName(file.getOriginalFilename());
            fileResource.setFilePath(filePath.toString());
            fileResource.setFileType(file.getContentType());
            fileResource.setSize(file.getSize());
            fileResource.setCourse(course);

            log.info("File uploaded: {} for course: {}", fileName, course.getTitle());
            return fileResourceRepository.save(fileResource);

        } catch (IOException e) {
            throw new BadRequestException("Failed to upload file: " + e.getMessage());
        }
    }

    public Resource downloadFile(Long fileId) {
        FileResource fileResource = fileResourceRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));

        try {
            Path filePath = Paths.get(fileResource.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new ResourceNotFoundException("File not found on disk");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new BadRequestException("Invalid file path");
        }
    }

    public List<FileResource> getFilesByCourse(Long courseId) {
        return fileResourceRepository.findByCourseId(courseId);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> processFileAsync(Long fileId) {
        log.info("Processing file async: {}", fileId);
        try {
            Thread.sleep(500);
            log.info("File processing done: {}", fileId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }
}
