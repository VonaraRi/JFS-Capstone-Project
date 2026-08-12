package com.example.courseenrolment.dto;

import java.util.List;

/*
Standard shape for error responses returned by the API.
This class encapsulates the HTTP status code, a message describing the error, 
and an optional list of field-specific validation errors.
 */

public class ApiErrorResponse {
    private String message;
    private List<FieldErrorDetail> errors;

    public ApiErrorResponse(String message) {
        this.message = message;
        this.errors = List.of(); // Initialize with an empty list if no field errors are provided
    }

    public ApiErrorResponse(String message, List<FieldErrorDetail> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldErrorDetail> getErrors() {
        return errors;
    }

}
