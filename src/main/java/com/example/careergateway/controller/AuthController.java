package com.example.careergateway.controller;

import com.example.careergateway.dto.ApiResponse;
import com.example.careergateway.dto.SignInRequest;
import com.example.careergateway.dto.SignUpRequest;
import com.example.careergateway.entity.User;
import com.example.careergateway.repository.UserRepository;
import com.example.careergateway.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    private static final String ADMIN_EMAIL = "vamsiklu367@gmail.com";
    private static final String ADMIN_PASSWORD = "Vamsi@126971";

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpRequest request) {
        logger.info("Sign-up attempt for email: {}", request.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Sign-up failed - user already exists: {}", request.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(false, "User already exists", null));
        }

        // Hash password with BCrypt
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .role("student")
                .build();

        User savedUser = userRepository.save(newUser);
        savedUser.setPassword(null); // Don't return password
        
        // Generate JWT
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole(), savedUser.getId());
        savedUser.setToken(token);

        logger.info("User registered successfully: {}", savedUser.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, null, savedUser));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<User>> signIn(@RequestBody SignInRequest request) {
        logger.info("Sign-in attempt for email: {}", request.getEmail());

        // Special handle for hardcoded admin access
        if (ADMIN_EMAIL.equals(request.getEmail()) && ADMIN_PASSWORD.equals(request.getPassword())) {
            User adminUser = User.builder()
                    .id(0L) // Dummy ID for admin
                    .name("Admin")
                    .email(ADMIN_EMAIL)
                    .role("admin")
                    .build();
                    
            String token = jwtUtil.generateToken(adminUser.getEmail(), adminUser.getRole(), adminUser.getId());
            adminUser.setToken(token);

            logger.info("Admin login successful");
            return ResponseEntity.ok(new ApiResponse<>(true, null, adminUser));
        }

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Allow legacy plain-text passwords to keep existing dev accounts working
            boolean passwordMatches = false;
            try {
                passwordMatches = BCrypt.checkpw(request.getPassword(), user.getPassword());
            } catch (Exception e) {
                logger.debug("BCrypt check failed, attempting legacy plain-text comparison for: {}", request.getEmail());
                // If it fails, maybe it's legacy plain text (not ideal for production but avoids locking out user during upgrade)
                if (request.getPassword().equals(user.getPassword())) {
                    passwordMatches = true;
                    // Auto-upgrade password
                    user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
                    userRepository.save(user);
                    logger.info("Password auto-upgraded to BCrypt for user: {}", request.getEmail());
                }
            }

            if (passwordMatches) {
                user.setPassword(null); // Don't return password
                // Generate JWT
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
                user.setToken(token);
                logger.info("Sign-in successful for user: {}", user.getEmail());
                return ResponseEntity.ok(new ApiResponse<>(true, null, user));
            }
        }

        logger.warn("Sign-in failed - invalid credentials for email: {}", request.getEmail());
        return ResponseEntity.status(401).body(new ApiResponse<>(false, "Invalid credentials", null));
        
    }
}
