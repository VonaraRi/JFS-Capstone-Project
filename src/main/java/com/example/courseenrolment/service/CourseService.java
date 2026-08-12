package com.example.courseenrolment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.courseenrolment.dto.CourseResponse;
import com.example.courseenrolment.dto.CreateTicketRequest;
import com.example.courseenrolment.exception.ResourceNotFoundException;

/*
Service contains business logic. Simple POJO annotated with
@Service so Spring manages it as a bean during startup.
*/
@Service
public class CourseService {
    private final List<CourseResponse> courses = new ArrayList<>();

    public CourseService() {
        // Initialize with sample courses
        courses.add(new CourseResponse(
            "C001",
            "CS101",
            "Software Engineering Fundamentals",
            "Learn core software development principles and object-oriented design.",
            "Software",
            "Beginner",
            "30",
            "Open",
            "2023-01-01"
        ));

        courses.add(new CourseResponse(
            "C002",
            "CS201",
            "Computer Architecture & Hardware",
            "Explore logic gates, CPU microarchitecture, and memory systems.",
            "Hardware",
            "Intermediate",
            "25",
            "Open",
            "2023-02-01"
        ));

        courses.add(new CourseResponse(
            "C003",
            "CS301",
            "Computer Networks & Protocols",
            "Understand TCP/IP model, routing protocols, and network security.",
            "Networking",
            "Advanced",
            "20",
            "Closed",
            "2023-03-01"
        ));

        courses.add(new CourseResponse(
            "C004",
            "CS401",
            "Database Systems & SQL",
            "Learn relational databases, SQL queries, and database design.",
            "Database",
            "Intermediate",
            "30",
            "Open",
            "2023-04-01"
        ));
    }

    public List<CourseResponse> getAllCourses() {
        return courses;
    }

    public CourseResponse getCourseById(String id) {
        return courses.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public CourseResponse createCourse(CreateTicketRequest request) {
        CourseResponse created = new CourseResponse(
            createNextId(),
            request.getCourseCode(),
            request.getTitle(),
            request.getDescription(),
            request.getCategory(),
            request.getLevel(),
            request.getMaxCapacity(),
            request.getStatus(),
            request.getCreatedAt()
        );
        courses.add(created);
        return created;
    }

    private String createNextId() {
        return "C" + String.format("%03d", courses.size() + 1);
    }
}