package com.example.courseenrolment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.courseenrolment.model.Enrolment;

public interface EnrolmentRepository extends MongoRepository<Enrolment, String> {
    boolean existsByUserIdAndCourseIdAndStatus(String userId, String courseId, String status);
    List<Enrolment> findByUserId(String userId);
    long countByCourseIdAndStatus(String courseId, String status);

    // Added to check for existing records (regardless of ENROLLED or UNROLLED status)
    Optional<Enrolment> findByUserIdAndCourseId(String userId, String courseId);
}