package com.example.courseenrolment.dto;

public class AuthResponse {
    private String token;
    private String tokenType;
    private long expiresInMinutes;
    private String studentId;
    private String name;
    private String email;
    private String role;

    public AuthResponse(String token, String tokenType, long expiresInMinutes, String studentId, String name, String email, String role){
        this.token = token;
        this.tokenType = tokenType;
        this.expiresInMinutes = expiresInMinutes;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}