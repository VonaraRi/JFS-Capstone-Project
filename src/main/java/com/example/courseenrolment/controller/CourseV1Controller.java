package com.example.courseenrolment.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateCourseRequest;
import com.example.courseenrolment.dto.UpdateCourseRequest;
import com.example.courseenrolment.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseV1Controller {
    
    private static final Set<String> ALLOWED_SORT_FIELD =
            Set.of("courseCode", "title", "category", "level", "capacity", "status", "createdAt");
    
    private final CourseService courseService;

    public CourseV1Controller(CourseService courseService) {
        this.courseService = courseService;
    }

    // GET /api/v1/courses -> Return list of courses
    @GetMapping
    public List<CourseResponse> getCourses(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String level,
        @RequestParam(required = false) String capacity
    ) {
        return courseService.getCourses(status, category, level, capacity);
    }

    // GET /api/v1/courses/paged -> Return paged courses
    @GetMapping("/paged")
    public Page<CourseResponse> getCoursePaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "courseCode") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page index must be 0 or greater");
        }

        // FIXED: Changed `page < 1` to `size < 1`
        if (size < 1 || size > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be between 1 and 50");
        }

        if (sortBy == null || sortBy.isBlank() || !ALLOWED_SORT_FIELD.contains(sortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort field. Allowed fields are: " + ALLOWED_SORT_FIELD);
        }

        String normalisedDirection = direction.toLowerCase();
        if (!Set.of("asc", "desc").contains(normalisedDirection)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort direction must be either 'asc' or 'desc'");
        }

        return courseService.getCoursePaged(page, size, sortBy, direction);
    }

    // GET /api/v1/courses/{id} -> Return a single course by ID
    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id);
    }
    
    // POST /api/v1/courses -> Create a new course
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }
    
    // PUT /api/v1/courses/{id} -> Update an existing course
    @PutMapping("/{id}")
    public CourseResponse updateCourse(
        @PathVariable String id,
        @Valid @RequestBody UpdateCourseRequest request) {
        
        return courseService.updateCourse(id, request);
    }
}