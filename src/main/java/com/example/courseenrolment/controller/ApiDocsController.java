package com.example.courseenrolment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.courseenrolment.dto.ApiDocumentationResponse;
import com.example.courseenrolment.dto.ApiEndpointResponse;

import java.util.List;

@RestController
@RequestMapping("/api/docs")
public class ApiDocsController {

    @GetMapping
    public ApiDocumentationResponse getDocumentation() {
        return new ApiDocumentationResponse(
            "Course Enrolment API",
            "v1",
            "/api/v1",
            List.of(
                // --- Public & Health Endpoints ---
                new ApiEndpointResponse("GET", "/api/class", "Public", "Check backend service status."),
                new ApiEndpointResponse("GET", "/api/v1/info", "Public", "Get API metadata and system information."),
                new ApiEndpointResponse("GET", "/api/docs", "Public", "Get interactive API documentation and endpoint directory."),

                // --- Auth Endpoints ---
                new ApiEndpointResponse("POST", "/api/auth/register", "Public", "Register a new user account."),
                new ApiEndpointResponse("POST", "/api/auth/login", "Public", "Login and receive a JWT bearer token."),

                // --- Unversioned Course Endpoints ---
                new ApiEndpointResponse("GET", "/api/courses", "STUDENT, ADMIN", "Fetch all courses or filter by query parameters (status, category, level, capacity)."),
                new ApiEndpointResponse("GET", "/api/courses/{id}", "STUDENT, ADMIN", "Fetch a single course by its unique MongoDB identifier."),
                new ApiEndpointResponse("GET", "/api/courses/paged", "STUDENT, ADMIN", "Fetch paginated courses with optional sorting (page, size, sortBy, direction)."),
                new ApiEndpointResponse("POST", "/api/courses", "ADMIN", "Create a new course entry."),

                // --- Version 1 Course Endpoints ---
                new ApiEndpointResponse("GET", "/api/v1/courses", "STUDENT, ADMIN", "Fetch all courses (v1 route)."),
                new ApiEndpointResponse("GET", "/api/v1/courses/{id}", "STUDENT, ADMIN", "Fetch a single course by ID (v1 route)."),

                // --- Aggregation & Analytics Reports ---
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-category", "ADMIN", "Generate an aggregation report counting courses grouped by category."),
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-level", "ADMIN", "Generate an aggregation report counting courses grouped by level."),
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-capacity", "ADMIN", "Generate an aggregation report counting courses grouped by capacity."),
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-status", "ADMIN", "Generate an aggregation report counting courses grouped by status.")
            )
        );
    }
}