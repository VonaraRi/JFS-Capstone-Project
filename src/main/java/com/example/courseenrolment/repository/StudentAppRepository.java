package com.example.courseenrolment.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.courseenrolment.model.StudentApp;

public interface StudentAppRepository extends MongoRepository<StudentApp, String> {
    Optional<StudentApp> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
