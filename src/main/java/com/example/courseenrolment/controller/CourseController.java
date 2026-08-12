package com.example.courseenrolment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateTicketRequest;
import com.example.courseenrolment.service.CourseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/courses")
public class CourseController {
    // Constructor injection is the recommended way to inject dependencies in Spring.
    // It makes the class easier to test and ensures that the dependency is not null.
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // GET ->Return list of courses
    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    // GET -> Return a single course by ID
    @GetMapping("{id}")
    public CourseResponse getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id);
    }
    
    // POST -> Create a new course
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CreateTicketRequest request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }
    
}