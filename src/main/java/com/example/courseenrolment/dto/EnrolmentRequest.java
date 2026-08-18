package com.example.courseenrolment.dto;

import jakarta.validation.constraints.NotBlank;

public class EnrolmentRequest {

    @NotBlank(message = "Course ID is required")
    private String courseId;

    public EnrolmentRequest() {
    }

    public EnrolmentRequest(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}