package com.example.courseenrolment.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateCourseRequest;
import com.example.courseenrolment.exception.ResourceNotFoundException;
import com.example.courseenrolment.model.Course;
import com.example.courseenrolment.repository.CourseRepository;

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public CourseResponse getCourseById(String id) {
        logger.info("Fetching course by id={}", id);

        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
            
        return toResponse(course);
    }

    public CourseResponse createCourse(CreateCourseRequest request) {
        String courseCode = request.getCourseCode().trim();

        if (courseRepository.existByCourseCode(courseCode)) {
            throw new DuplicateResourseException("Course code already exist: " + courseCode);
        }

        // Map DTO Request to Course Entity using constructor
        Course course = new Course(
            request.getCourseCode().trim(),
            request.getTitle().trim(),
            request.getDescription().trim(),
            request.getCategory().trim(),
            request.getLevel().trim(),
            request.getMaxCapacity().trim(),
            "OPEN", // Backend sets default status
            (request.getCreatedAt() != null && !request.getCreatedAt().isBlank()) 
                ? request.getCreatedAt().trim()
                : LocalDate.now().toString()
        );

        // Save entity to MongoDB
        Course savedCourse = courseRepository.save(course);

        return toResponse(savedCourse);
    }

    // Helper method to convert Course entity to CourseResponse DTO
    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getCourseCode(),
            course.getTitle(),
            course.getDescription(),
            course.getCategory(),
            course.getLevel(),
            course.getMaxCapacity(),
            course.getStatus(),
            course.getCreatedAt()
        );
    }
}