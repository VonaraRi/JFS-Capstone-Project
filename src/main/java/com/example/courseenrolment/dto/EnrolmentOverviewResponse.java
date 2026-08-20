package com.example.courseenrolment.dto;

import java.util.List;

public class EnrolmentOverviewResponse {
    private List<ReportCountResponse> level;
    private List<ReportCountResponse> status;
    private List<ReportCountResponse> category;
    private List<ReportCountResponse> capacity;
    private long totalCourses;

    public EnrolmentOverviewResponse() {}

    public EnrolmentOverviewResponse(List<ReportCountResponse> level,
                                    List<ReportCountResponse> status,
                                    List<ReportCountResponse> category,
                                    List<ReportCountResponse> capacity,
                                    long totalCourses) {
        this.level = level;
        this.status = status;
        this.category = category;
        this.capacity = capacity;
        this.totalCourses = totalCourses;
    }

    // Getters and Setters
    public List<ReportCountResponse> getLevel() { return level; }
    public void setLevel(List<ReportCountResponse> level) { this.level = level; }

    public List<ReportCountResponse> getStatus() { return status; }
    public void setStatus(List<ReportCountResponse> status) { this.status = status; }

    public List<ReportCountResponse> getCategory() { return category; }
    public void setCategory(List<ReportCountResponse> category) { this.category = category; }

    public List<ReportCountResponse> getCapacity() { return capacity; }
    public void setCapacity(List<ReportCountResponse> capacity) { this.capacity = capacity; }

    public long getTotalCourses() { return totalCourses; }
    public void setTotalCourses(long totalCourses) { this.totalCourses = totalCourses; }
}