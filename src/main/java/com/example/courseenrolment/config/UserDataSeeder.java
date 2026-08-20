package com.example.courseenrolment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.courseenrolment.model.AppUser;
import com.example.courseenrolment.repository.AppUserRepository;

@Configuration
public class UserDataSeeder {
    
    private final AppUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDataSeeder.class);

    UserDataSeeder(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner seedUsers(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Admin Account
            createUserIfMissing(
                    userRepository,
                    passwordEncoder,
                    "Admin User",
                    "admin@example.com",
                    "Admin@12345",
                    "ADMIN"
            );

            // Seed Default Student Account
            createUserIfMissing(
                    userRepository,
                    passwordEncoder,
                    "Student User",
                    "student@example.com",
                    "Student@12345",
                    "STUDENT"
            );

            // --- Seed 10 Student Accounts ---
            createUserIfMissing(userRepository, passwordEncoder, "Jason Lee", "jason.lee@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Alice Smith", "alice.smith@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Bob Tan", "bob.tan@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Catherine Wong", "catherine.wong@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "David Miller", "david.miller@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Emma Watson", "emma.watson@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Fiona Chen", "fiona.chen@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "George Ibrahim", "george.ibrahim@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Hannah Taylor", "hannah.taylor@example.com", "Student@12345", "STUDENT");
            createUserIfMissing(userRepository, passwordEncoder, "Ian Wright", "ian.wright@example.com", "Student@12345", "STUDENT");
        };
    }

    private void createUserIfMissing(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String name,
            String email,
            String rawPassword,
            String role) {

        if (userRepository.existsByEmailIgnoreCase(email)) {
            logger.info("Seed user already exists: {}", email);
            return;
        }

        AppUser user = new AppUser(
                name,
                email.toLowerCase(),
                passwordEncoder.encode(rawPassword),
                role
        );

        userRepository.save(user);
        logger.info("Seeder created user email={} role={}", user.getEmail(), user.getRole());
    }
}