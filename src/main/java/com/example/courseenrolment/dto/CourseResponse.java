package com.example.courseenrolment.dto;

public class CourseResponse {
    private String id;
    private String courseCode;
    private String title;
    private String description;
    private String category;
    private String level;
    private String maxCapacity;
    private String status;
    private String createdAt;

    public CourseResponse(String id, String courseCode, String title, String description, String category, String level, String maxCapacity, String status, String createdAt) {
        this.id = id;
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.category = category;
        this.level = level;
        this.maxCapacity = maxCapacity;
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

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
