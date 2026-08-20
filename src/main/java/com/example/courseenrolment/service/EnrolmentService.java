package com.example.courseenrolment.service;

import com.example.courseenrolment.dto.EnrolmentRequest;
import com.example.courseenrolment.dto.EnrolmentResponse;
import com.example.courseenrolment.exception.DuplicateResourseException;
import com.example.courseenrolment.exception.ResourceNotFoundException;
import com.example.courseenrolment.model.Course;
import com.example.courseenrolment.model.Enrolment;
import com.example.courseenrolment.model.AppUser;
import com.example.courseenrolment.repository.AppUserRepository;
import com.example.courseenrolment.repository.CourseRepository;
import com.example.courseenrolment.repository.EnrolmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final CourseRepository courseRepository;
    private final AppUserRepository appUserRepository;

    public EnrolmentService(EnrolmentRepository enrolmentRepository, CourseRepository courseRepository, AppUserRepository appUserRepository) {
        this.enrolmentRepository = enrolmentRepository;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
    }

    public EnrolmentResponse enrollStudent(String userId, EnrolmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        // Check if course is INACTIVE
        if ("INACTIVE".equalsIgnoreCase(course.getStatus())) {
            throw new IllegalStateException("Cannot enroll in an inactive course.");
        }

        // 1. Check if an enrolment record already exists (ENROLLED or UNROLLED)
        Enrolment enrolment = enrolmentRepository.findByUserIdAndCourseId(userId, request.getCourseId())
                .orElse(null);

        // 2. Prevent double active enrolments
        if (enrolment != null && "ENROLLED".equalsIgnoreCase(enrolment.getStatus())) {
            throw new DuplicateResourseException("You are already enrolled in this course.");
        }

        // 3. Validate capacity limit
        long activeEnrolmentsCount = enrolmentRepository.countByCourseIdAndStatus(request.getCourseId(), "ENROLLED");
        if ("FULL".equalsIgnoreCase(course.getCapacity()) || activeEnrolmentsCount >= extractCapacityNumber(course.getCapacity())) {
            throw new IllegalStateException("Course has reached maximum capacity.");
        }

        // 4. Update existing document if previously UNROLLED, or create a new one if first time
        if (enrolment != null) {
            enrolment.setStatus("ENROLLED");
            enrolment.setEnrolmentDate(java.time.Instant.now()); // Refreshes timestamp as Instant
        } else {
            enrolment = new Enrolment(userId, course.getId());
            enrolment.setStatus("ENROLLED");
            enrolment.setEnrolmentDate(java.time.Instant.now());
        }

        Enrolment savedEnrolment = enrolmentRepository.save(enrolment);
        AppUser user = appUserRepository.findById(userId).orElse(null);

        return mapToResponse(savedEnrolment, course, user);
    }

    public List<EnrolmentResponse> getMyEnrolments(String userId) {
        List<Enrolment> enrolments = enrolmentRepository.findByUserId(userId);
        AppUser user = appUserRepository.findById(userId).orElse(null);

        return enrolments.stream()
            .filter(enrolment -> "ENROLLED".equalsIgnoreCase(enrolment.getStatus()))
            .map(enrolment -> {
            Course course = courseRepository.findById(enrolment.getCourseId()).orElse(null);
            return mapToResponse(enrolment, course, user);
        }).toList();
    }

    public void dropEnrolment(String userId, String enrolmentId) {
        Enrolment enrolment = enrolmentRepository.findById(enrolmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrolment not found with id: " + enrolmentId));

        if (!enrolment.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to drop this enrolment.");
        }

        enrolment.setStatus("UNROLLED");
        enrolmentRepository.save(enrolment);
    }

    private EnrolmentResponse mapToResponse(Enrolment enrolment, Course course, AppUser user) {
        String courseCode = (course != null) ? course.getCourseCode() : "N/A";
        String courseTitle = (course != null) ? course.getTitle() : "N/A";
        String userName = (user != null) ? user.getName() : "N/A";

        return new EnrolmentResponse(
                enrolment.getId(),
                enrolment.getUserId(),
                userName,
                enrolment.getCourseId(),
                courseCode,
                courseTitle,
                enrolment.getEnrolmentDate(),
                enrolment.getStatus()
        );
    }

    private int extractCapacityNumber(String capacityStr) {
        if (capacityStr == null || capacityStr.isBlank() || "FULL".equalsIgnoreCase(capacityStr.trim())) {
            return 0;
        }
        try {
            String numberOnly = capacityStr.replaceAll("[^0-9]", "");
            return numberOnly.isEmpty() ? 0 : Integer.parseInt(numberOnly);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}