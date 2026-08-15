package com.example.courseenrolment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.courseenrolment.model.Course;

public interface CourseRepository extends MongoRepository<Course, String> {
    /*
    Custom query methods can be defined here if needed
    /api/courses/{id}
    /api/courses?status=OPEN filter courses by status
    /api/courses?category=Software filter courses by category
    /api/courses?page=18&size=10 control page and size
    findByStatusIgnoreCase("OPEN")
    {
        "status": "ACTIVE"
    }
    */
    
    List<Course> findByStatusIgnoreCase(String status);

    List<Course> findByCategoryIgnoreCase(String category);

    List<Course> findByLevelIgnoreCase(String level);

    List<Course> findByCapacity(String capacity);

    boolean existsByCourseCode(String courseCode);

    boolean existsByCreatedAt(String createdAt);
}
