package com.example.finalproject.controller;

import com.example.finalproject.entity.FileResource;
import com.example.finalproject.service.NurbolatDjumadilovFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class NurbolatDjumadilovFileController {

    private final NurbolatDjumadilovFileService fileService;

    @PostMapping("/upload/course/{courseId}")
    public ResponseEntity<FileResource> upload(@RequestParam("file") MultipartFile file,
                                               @PathVariable Long courseId) {
        return ResponseEntity.ok(fileService.uploadFile(file, courseId));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        Resource resource = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<FileResource>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(fileService.getFilesByCourse(courseId));
    }
}
