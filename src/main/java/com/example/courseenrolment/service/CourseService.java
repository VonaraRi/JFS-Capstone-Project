package com.example.courseenrolment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateCourseRequest;
import com.example.courseenrolment.dto.UpdateCourseRequest;
import com.example.courseenrolment.exception.DuplicateResourseException;
import com.example.courseenrolment.exception.InvalidRequestException;
import com.example.courseenrolment.exception.ResourceNotFoundException;
import com.example.courseenrolment.model.Course;
import com.example.courseenrolment.repository.CourseRepository;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private static final Set<String> ALLOWED_STATUS = Set.of("ACTIVE", "INACTIVE");
    private static final Set<String> ALLOWED_LEVEL = Set.of("Beginner", "Intermediate", "Advanced");

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
            courses = courseRepository.findByCapacity(capacity.trim());
        } else {
            courses = courseRepository.findAll();
        }

        logger.info("Found {} course(s)", courses.size());

        return courses.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<CourseResponse> getCoursePaged(int page, int size, String sortBy, String direction) {
        logger.info("Fetching paged courses page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

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
        logger.info("Creating course with code={}", request.getCourseCode());

        String courseCode = safeTrim(request.getCourseCode());

        if (courseRepository.existsByCourseCode(courseCode)) {
            throw new DuplicateResourseException("Course code already exists: " + courseCode);
        }

        String level = safeTrim(request.getLevel());
        if (hasValue(level)) {
            validateLevel(level);
        }

        Course course = new Course(
            courseCode,
            safeTrim(request.getTitle()),
            safeTrim(request.getDescription()),
            safeTrim(request.getCategory()),
            level,
            safeTrim(request.getCapacity()),
            "ACTIVE", // Default status
            hasValue(request.getCreatedAt()) ? request.getCreatedAt().trim() : LocalDate.now().toString()
        );

        Course savedCourse = courseRepository.save(course);
        return toResponse(savedCourse);
    }

    public CourseResponse updateCourse(String id, UpdateCourseRequest request) {
        logger.info("Updating course id={}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        String courseCode = safeTrim(request.getCourseCode());
        String status = safeTrim(request.getStatus()).toUpperCase();
        String level = safeTrim(request.getLevel());

        validateStatus(status);
        validateLevel(level);

        // Check if updating to a course code that belongs to another entity
        if (!course.getCourseCode().equalsIgnoreCase(courseCode) && courseRepository.existsByCourseCode(courseCode)) {
            throw new DuplicateResourseException("Course code already exists: " + courseCode);
        }

        course.setCourseCode(courseCode);
        course.setTitle(safeTrim(request.getTitle()));
        course.setDescription(safeTrim(request.getDescription()));
        course.setCategory(safeTrim(request.getCategory()));
        course.setLevel(level);
        course.setCapacity(safeTrim(request.getCapacity()));
        course.setStatus(status);
        
        if (hasValue(request.getCreatedAt())) {
            course.setCreatedAt(request.getCreatedAt().trim());
        }

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

    // Helper methods
    private boolean hasValue(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private String safeTrim(String str) {
        return str == null ? "" : str.trim();
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUS.contains(status)) {
            throw new InvalidRequestException("Status must be ACTIVE or INACTIVE");
        }
    }

    private void validateLevel(String level) {
        if (!ALLOWED_LEVEL.contains(level)) {
            throw new InvalidRequestException("Level must be Beginner, Intermediate, or Advanced");
        }
    }
}