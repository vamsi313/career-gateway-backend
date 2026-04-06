package com.example.careergateway.controller;

import com.example.careergateway.dto.ApiResponse;
import com.example.careergateway.dto.SignInRequest;
import com.example.careergateway.dto.SignUpRequest;
import com.example.careergateway.entity.User;
import com.example.careergateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private static final String ADMIN_EMAIL = "vamsiklu367@gmail.com";
    private static final String ADMIN_PASSWORD = "Vamsi@126971";

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "User already exists", null));
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // Plain text as requested to keep simple without JWT/encryption
                .role("student")
                .build();

        User savedUser = userRepository.save(newUser);
        savedUser.setPassword(null); // Don't return password
        
        return ResponseEntity.ok(new ApiResponse<>(true, null, savedUser));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<User>> signIn(@RequestBody SignInRequest request) {
        if (ADMIN_EMAIL.equals(request.getEmail())) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Use Admin Sign In for administrator access", null));
        }

        Optional<User> userOptional = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(null); // Don't return password
            return ResponseEntity.ok(new ApiResponse<>(true, null, user));
        }
        
        return ResponseEntity.ok(new ApiResponse<>(false, "Invalid email or password", null));
    }

    @PostMapping("/admin-signin")
    public ResponseEntity<ApiResponse<User>> adminSignIn(@RequestBody SignInRequest request) {
        if (ADMIN_EMAIL.equals(request.getEmail()) && ADMIN_PASSWORD.equals(request.getPassword())) {
            User adminUser = User.builder()
                    .id(0L) // Dummy ID for admin
                    .name("Admin")
                    .email(ADMIN_EMAIL)
                    .role("admin")
                    .build();
            return ResponseEntity.ok(new ApiResponse<>(true, null, adminUser));
        }
        
        return ResponseEntity.ok(new ApiResponse<>(false, "Invalid admin credentials", null));
    }
}
