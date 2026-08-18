package com.example.courseenrolment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.courseenrolment.dto.ReportCountResponse;
import com.example.courseenrolment.service.CourseReportService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    
    private final CourseReportService courseReportService;

    public ReportController(CourseReportService  courseReportService) {
        this.courseReportService = courseReportService;
    }

    @GetMapping("/courses-by-category")
    public List<ReportCountResponse> getCoursesByCategory() {
        return courseReportService.countCourseByCategory();
    }
    
    @GetMapping("/courses-by-level")
    public List<ReportCountResponse> getCoursesByLevel() {
        return courseReportService.countCourseByLevel();
    }

    @GetMapping("/courses-by-capacity")
    public List<ReportCountResponse> getCoursesByCapacity() {
        return courseReportService.countCourseByCapacity();
    }

    @GetMapping("/courses-by-status")
    public List<ReportCountResponse> getCoursesByStatus() {
        return courseReportService.countCourseByStatus();
    }
}
