package com.example.courseenrolment.controller;

import com.example.courseenrolment.dto.EnrolmentRequest;
import com.example.courseenrolment.dto.EnrolmentResponse;
import com.example.courseenrolment.service.EnrolmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrolments")
public class EnrolmentV1Controller {

    private final EnrolmentService enrolmentService;

    public EnrolmentV1Controller(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    // POST /api/v1/enrolments -> Enroll student into a course
    @PostMapping
    public ResponseEntity<EnrolmentResponse> enrollStudent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EnrolmentRequest request) {

        String userId = extractUserId(jwt);
        EnrolmentResponse response = enrolmentService.enrollStudent(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/enrolments/my-courses -> Return enrolments for current student
    @GetMapping("/my-courses")
    public List<EnrolmentResponse> getMyEnrolments(@AuthenticationPrincipal Jwt jwt) {
        String userId = extractUserId(jwt);
        return enrolmentService.getMyEnrolments(userId);
    }

    // PUT /api/v1/enrolments/{enrolmentId}/drop -> Drop/Unenroll from a course
    @PutMapping("/{enrolmentId}/drop")
    public ResponseEntity<Void> dropEnrolment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String enrolmentId) {

        String userId = extractUserId(jwt);
        enrolmentService.dropEnrolment(userId, enrolmentId);
        return ResponseEntity.noContent().build();
    }

    // Helper method to retrieve userId claim from JWT token
    private String extractUserId(Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        if (userId == null || userId.isBlank()) {
            return jwt.getSubject();
        }
        return userId;
    }
}