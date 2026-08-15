package com.example.courseenrolment.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateCourseRequest;
import com.example.courseenrolment.exception.DuplicateResourseException;
import com.example.courseenrolment.exception.ResourceNotFoundException;
import com.example.courseenrolment.model.Course;
import com.example.courseenrolment.repository.CourseRepository;

/*
Services contain business logic.
To query MongoDB documents, add filtering, add pagination/sorting, and
log important service operations

validation utk pengesahan
*/

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class); // track system activity/event/userEvent...

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> getCourses(String status, String category, String level, String capacity) {
        logger.info("Fetching courses with status={}, category={}, level={}, capacity={}", status, category, level, capacity);

        List<Course> courses;

        if (hasValue(status)) {
            courses = courseRepository.findByStatusIgnoreCase(status.trim());
        } else if (hasValue(category)) {
            courses = courseRepository.findByCategoryIgnoreCase(category.trim());
        } else if (hasValue(level)) {
            courses = courseRepository.findByLevelIgnoreCase(level.trim());
        } else if (hasValue(capacity)) {
            courses = courseRepository.findByCapacity(capacity);
        } else {
            courses = courseRepository.findAll();
        }

        logger.info("Found {} course(s)", courses.size());

        return courses.stream()
            .map(this::toResponse)
            .toList();
    }

    public Page<CourseResponse> getCoursePaged(int page, int size, String sortBy, String direction) {
        logger.info("Fetching paged course page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return courseRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public CourseResponse getCourseById(String id) {
        logger.info("Fetching course by id={}", id);

        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
            
        return toResponse(course);
    }

    public CourseResponse createCourse(CreateCourseRequest request) {
        String courseCode = request.getCourseCode().trim();

        if (courseRepository.existsByCourseCode(courseCode)) {
            throw new DuplicateResourseException("Course code already exist: " + courseCode);
        }

        // Map DTO Request to Course Entity using constructor
        Course course = new Course(
            request.getCourseCode().trim(),
            request.getTitle().trim(),
            request.getDescription().trim(),
            request.getCategory().trim(),
            request.getLevel().trim(),
            request.getCapacity().trim(),
            "ACTIVE", // Backend sets default status
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
            course.getCapacity(),
            course.getStatus(),
            course.getCreatedAt()
        );
    }

    // Helper method to check if string parameter is non-null and not empty
    private boolean hasValue(String str) {
        return str != null && !str.trim().isEmpty();
    }
}