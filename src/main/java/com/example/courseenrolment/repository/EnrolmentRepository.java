package com.example.courseenrolment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.courseenrolment.model.Enrolment;

public interface EnrolmentRepository extends MongoRepository<Enrolment, String> {
    boolean existsByUserIdAndCourseIdAndStatus(String userId, String courseId, String status);
    List<Enrolment> findByUserId(String userId);
    long countByCourseIdAndStatus(String courseId, String status);
}
