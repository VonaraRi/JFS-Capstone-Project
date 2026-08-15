package com.example.courseenrolment.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateCourseRequest;
import com.example.courseenrolment.service.CourseService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
Controllers handle HTTP requests and return reponses.
They are "web layer" of a Spring Boot. Typical pattern:
- Annotate with @RestController to expose JSON endpoints
- Inject a Service to perform business logic (separation of concerns)
*/

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
    public List<CourseResponse> getCourses(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String level,
        @RequestParam(required = false) String capacity
    ) {
        return courseService.getCourses(status, category, level, capacity);
    }

    // GET -> Return paged
    @GetMapping("/paged")
    public Page<CourseResponse> getCoursePaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "courseCode") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        return courseService.getCoursePaged(page, size, sortBy, direction);
    }
    

    // GET -> Return a single course by ID
    @GetMapping("{id}")
    public CourseResponse getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id);
    }
    
    // POST -> Create a new course. @Valid triggers validation annotations
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }
    
}