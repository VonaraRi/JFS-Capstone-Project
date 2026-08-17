package com.example.courseenrolment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.courseenrolment.dto.AuthResponse;
import com.example.courseenrolment.dto.LoginRequest;
import com.example.courseenrolment.dto.RegisterRequest;
import com.example.courseenrolment.exception.DuplicateResourseException;
import com.example.courseenrolment.model.StudentApp;
import com.example.courseenrolment.repository.StudentAppRepository;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final StudentAppRepository studentAppRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            StudentAppRepository studentAppRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.studentAppRepository = studentAppRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (studentAppRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourseException("Email already exists: " + email);
        }

        StudentApp student = new StudentApp(
                    request.getName().trim(),
                    email,
                    passwordEncoder.encode(request.getPassword()),
                    "STUDENT"
        );

        StudentApp savedStudent = studentAppRepository.save(student);
        logger.info("Registered new student email={} role={}", savedStudent.getEmail(), savedStudent.getRole());

        return buildAuthResponse(savedStudent);
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        StudentApp student = studentAppRepository.findByEmailIgnoreCase(email)
                .orElseThrow();

        logger.info("Student logged in email={} role={}", student.getEmail(), student.getRole());

        return buildAuthResponse(student);
    }

    public AuthResponse buildAuthResponse(StudentApp student) {
        String token = jwtService.generateToken(student);

        return new AuthResponse(token,
            "Bearer",
            jwtService.getExpirationMinutes(),
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getRole()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
