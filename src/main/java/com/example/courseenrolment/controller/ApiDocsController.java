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
                // --- Public & System Info Endpoints ---
                new ApiEndpointResponse("GET", "/api/v1/info", "Public", "Get API metadata and system status information."),
                new ApiEndpointResponse("GET", "/api/docs", "Public", "Get interactive API documentation and endpoint directory."),

                // --- Auth Endpoints ---
                new ApiEndpointResponse("POST", "/api/auth/register", "Public", "Register a new student account."),
                new ApiEndpointResponse("POST", "/api/auth/login", "Public", "Login to receive a JWT bearer token."),

                // --- Legacy / Unversioned Course Endpoints ---
                new ApiEndpointResponse("GET", "/api/courses", "STUDENT, ADMIN", "Fetch all courses or filter by parameters (status, category, level, capacity)."),
                new ApiEndpointResponse("GET", "/api/courses/paged", "STUDENT, ADMIN", "Fetch paginated courses with sorting."),
                new ApiEndpointResponse("GET", "/api/courses/{id}", "STUDENT, ADMIN", "Fetch a single course by ID."),
                new ApiEndpointResponse("POST", "/api/courses", "ADMIN", "Create a new course entry."),

                // --- Version 1 Course Endpoints ---
                new ApiEndpointResponse("GET", "/api/v1/courses", "STUDENT, ADMIN", "Fetch all courses (v1 route)."),
                new ApiEndpointResponse("GET", "/api/v1/courses/paged", "STUDENT, ADMIN", "Fetch paginated courses with field validation (v1 route)."),
                new ApiEndpointResponse("GET", "/api/v1/courses/{id}", "STUDENT, ADMIN", "Fetch a single course by ID (v1 route)."),
                new ApiEndpointResponse("POST", "/api/v1/courses", "ADMIN", "Create a new course (v1 route)."),
                new ApiEndpointResponse("PUT", "/api/v1/courses/{id}", "ADMIN", "Update an existing course by ID."),

                // --- Version 1 Enrolment Endpoints ---
                new ApiEndpointResponse("POST", "/api/v1/enrolments", "STUDENT", "Enroll authenticated student into a course."),
                new ApiEndpointResponse("GET", "/api/v1/enrolments/my-courses", "STUDENT", "Fetch enrolled courses for the logged-in student."),
                new ApiEndpointResponse("PUT", "/api/v1/enrolments/{enrolmentId}/drop", "STUDENT", "Drop/unenroll from a specific enrolment."),

                // --- Aggregation & Analytics Reports ---
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-category", "ADMIN", "Generate report counting courses grouped by category."),
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-level", "ADMIN", "Generate report counting courses grouped by difficulty level."),
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-capacity", "ADMIN", "Generate report counting courses grouped by capacity."),
                new ApiEndpointResponse("GET", "/api/v1/reports/courses-by-status", "ADMIN", "Generate report counting courses grouped by status."),
                new ApiEndpointResponse("GET", "/api/v1/reports/enrolments-per-course", "ADMIN", "Generate report counting total enrolments per course."),
                new ApiEndpointResponse("GET", "/api/v1/reports/popular-courses", "ADMIN", "Fetch top performing / most popular courses based on enrolment count."),
                new ApiEndpointResponse("GET", "/api/v1/reports/enrolments-overview", "ADMIN", "Fetch overview summary metrics of total enrolments."),
                new ApiEndpointResponse("GET", "/api/v1/reports/monthly-enrolments", "ADMIN", "Fetch monthly trend report for course enrolments.")
            )
        );
    }
}