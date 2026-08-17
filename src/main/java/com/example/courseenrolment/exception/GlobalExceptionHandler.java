package com.example.courseenrolment.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.courseenrolment.dto.FieldErrorDetail;
import com.example.courseenrolment.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Map ResourceNotFoundException to a 404 Not Found response
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ApiErrorResponse(exception.getMessage());
    }

    // Map DuplicateResourseException to a 409 Conflict response
    @ExceptionHandler(DuplicateResourseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleDuplicateResourceException(DuplicateResourseException exception) {
        return new ApiErrorResponse(exception.getMessage());
    }

    // Map validation errors (triggered by @Valid) to a 400 Bad Request response
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<FieldErrorDetail> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ApiErrorResponse("Validation failed for one or more fields.", errors);
    }
}