package com.example.courseenrolment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApiInfoController {
    
    @GetMapping("/info")
    public Map<String, Object> getApiInfor() {
        return Map.of(
            "application", "Course Enrolment API",
            "version", "v1",
            "status", "active",
            "documentation", "/api/docs",
            "reports", "/api/v1/reports/courses/courses-by-status",
            "description", "API for course enrolment"
        );
    }
    
}
