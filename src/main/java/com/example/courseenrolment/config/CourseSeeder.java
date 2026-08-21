package com.example.courseenrolment.config;

import com.example.courseenrolment.model.Course;
import com.example.courseenrolment.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class CourseSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;

    public CourseSeeder(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) {
        List<Course> newSeedCourses = List.of(
            createCourse("SE101", "Software Testing & Quality Assurance", "Learn unit testing, integration testing, TDD, and automated testing frameworks.", "Software Engineering", "Beginner", "15 Seats Left", "ACTIVE"),
            createCourse("SE102", "Software Architecture & Design Patterns", "Master object-oriented design principles and microservices architecture.", "Software Engineering", "Advanced", "FULL", "ACTIVE"),
            createCourse("DE101", "Data Pipelines & ETL Engineering", "Build scalable data ingestion pipelines and ETL workflows using Apache Airflow.", "Data Engineering", "Intermediate", "3 Seats Left", "ACTIVE"),
            createCourse("DB101", "Database Systems & SQL", "Master relational database design, normalization, complex SQL queries, and NoSQL.", "Database", "Beginner", "25 Seats Left", "ACTIVE"),
            createCourse("AI101", "Artificial Intelligence & Machine Learning", "Introduction to AI concepts, predictive analytics, and neural networks using Python.", "Artificial Intelligence", "Advanced", "2 Seats Left", "ACTIVE")
        );

        int addedCount = 0;
        for (Course course : newSeedCourses) {
            if (!courseRepository.existsByCourseCode(course.getCourseCode())) {
                courseRepository.save(course);
                addedCount++;
            }
        }

        System.out.println("CourseSeeder: Added " + addedCount + " new seeded courses. Total courses in DB: " + courseRepository.count());
    }

    private Course createCourse(String code, String title, String description, String category, String level, String capacity, String status) {
        Course course = new Course();
        course.setCourseCode(code);
        course.setTitle(title);
        course.setDescription(description);
        course.setCategory(category);
        course.setLevel(level);
        course.setCapacity(capacity);
        course.setStatus(status);
        course.setCreatedAt(Instant.now().toString());
        return course;
    }
}