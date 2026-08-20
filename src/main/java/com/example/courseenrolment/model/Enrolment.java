package com.example.courseenrolment.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "enrolments")
public class Enrolment {

    @Id
    private String id;
    private String userId;   // References the _id in the 'users' collection
    private String courseId; // References the _id in the 'courses' collection
    private Instant enrolmentDate;
    private String status;   // "ENROLLED", "UNROLLED"

    public Enrolment() {
        this.enrolmentDate = Instant.now();
        this.status = "ENROLLED";
    }

    public Enrolment(String userId, String courseId) {
        this();
        this.userId = userId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public Instant getEnrolmentDate() { return enrolmentDate; }
    public void setEnrolmentDate(Instant enrolmentDate) { this.enrolmentDate = enrolmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}