package com.example.courseenrolment.dto;

import java.util.List;

public class ReportCountResponse {
    
    private String label;
    private long count;
    private List<String> studentNames;

    public ReportCountResponse() {
    }

    public ReportCountResponse(String label, long count, List<String> studentNames) {
        this.label = label;
        this.count = count;
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }
}
