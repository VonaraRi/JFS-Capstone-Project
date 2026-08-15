package com.example.courseenrolment.dto;

public class CourseResponse {
    private String id;
    private String courseCode;
    private String title;
    private String description;
    private String category;
    private String level;
    private String capacity;
    private String status;
    private String createdAt;

    public CourseResponse(String id, String courseCode, String title, String description, String category, String level, String capacity, String status, String createdAt) {
        this.id = id;
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.category = category;
        this.level = level;
        this.capacity = capacity;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getLevel() {
        return level;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
