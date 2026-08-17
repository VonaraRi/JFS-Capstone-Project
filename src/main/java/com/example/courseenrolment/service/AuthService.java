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
import com.example.courseenrolment.model.AppUser;
import com.example.courseenrolment.repository.AppUserRepository;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourseException("Email already exists: " + email);
        }

        AppUser user = new AppUser(
                request.getName().trim(),
                email,
                passwordEncoder.encode(request.getPassword()),
                "STUDENT"
        );

        AppUser savedUser = userRepository.save(user);
        logger.info("Registered new user email={} role={}", savedUser.getEmail(), savedUser.getRole());

        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );
        
        AppUser user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow();

        logger.info("User logged in email={} role={}", user.getEmail(), user.getRole());

        return buildAuthResponse(user);
    }

    public AuthResponse buildAuthResponse(AppUser user) {
        String token = jwtService.generateToken(user);

        return new AuthResponse(
            token,
            "Bearer",
            jwtService.getExpirationMinutes(),
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}