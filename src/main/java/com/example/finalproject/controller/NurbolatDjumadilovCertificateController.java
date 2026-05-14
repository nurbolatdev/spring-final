package com.example.finalproject.controller;

import com.example.finalproject.service.NurbolatDjumadilovEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Tag(name = "Certificates", description = "Course completion certificates")
@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class NurbolatDjumadilovCertificateController {

    private final NurbolatDjumadilovEmailService emailService;

    @Operation(summary = "Generate certificate for completed course")
    @GetMapping("/course/{courseTitle}")
    public CompletableFuture<ResponseEntity<String>> getCertificate(
            @PathVariable String courseTitle,
            @AuthenticationPrincipal UserDetails userDetails) {
        return emailService.generateCertificate(userDetails.getUsername(), courseTitle)
                .thenApply(ResponseEntity::ok);
    }
}
