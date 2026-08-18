package com.example.courseenrolment.dto;

import java.time.Instant;

public class EnrolmentResponse {

    private String id;
    private String userId;
    private String userName;
    private String courseId;
    private String courseCode;
    private String courseTitle;
    private Instant enrolmentDate;
    private String status;

    public EnrolmentResponse() {
    }

    public EnrolmentResponse(String id, String userId, String userName, String courseId, String courseCode, String courseTitle, Instant enrolmentDate, String status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.enrolmentDate = enrolmentDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Instant getEnrolmentDate() {
        return enrolmentDate;
    }

    public void setEnrolmentDate(Instant enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}