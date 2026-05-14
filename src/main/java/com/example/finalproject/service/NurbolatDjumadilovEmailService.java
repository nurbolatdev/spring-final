package com.example.finalproject.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovEmailService {

    private static final Logger log = LoggerFactory.getLogger(NurbolatDjumadilovEmailService.class);

    @Async("taskExecutor")
    public CompletableFuture<Void> sendWelcomeEmail(String email, String username) {
        log.info("Sending welcome email to: {}", email);
        try {
            Thread.sleep(500);
            log.info("Welcome email sent to: {} (user: {})", email, username);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Email sending interrupted for: {}", email);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> sendEnrollmentConfirmation(String email, String courseTitle) {
        log.info("Sending enrollment confirmation to: {} for course: {}", email, courseTitle);
        try {
            Thread.sleep(300);
            log.info("Enrollment email sent to: {} for course: {}", email, courseTitle);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Enrollment email interrupted for: {}", email);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> generateCertificate(String username, String courseTitle) {
        log.info("Generating certificate for user: {} course: {}", username, courseTitle);
        try {
            Thread.sleep(1000);
            String certificate = "CERTIFICATE-" + username.toUpperCase() + "-" + courseTitle.hashCode();
            log.info("Certificate generated: {}", certificate);
            return CompletableFuture.completedFuture(certificate);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
}
