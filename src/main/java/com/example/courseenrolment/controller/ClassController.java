package com.example.courseenrolment.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ClassController {

    @GetMapping("/api/class")
    public Map<String, String> getClassDetails() {
        return Map.of(
            "status", "is it working?",
            "service", "course-enrolment-api"
        );
    }
}
