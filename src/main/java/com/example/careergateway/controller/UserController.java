package com.example.careergateway.controller;

import com.example.careergateway.dto.ApiResponse;
import com.example.careergateway.entity.User;
import com.example.careergateway.repository.AssessmentResultRepository;
import com.example.careergateway.repository.UserRepository;
import com.example.careergateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentResultRepository assessmentResultRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private boolean isAuthorized(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateToken(token);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long userId, 
            @RequestBody User updatedUser,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
            
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Unauthorized", null));
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "User not found", null));
        }

        User user = userOpt.get();
        // Update allowed fields
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        // For simplicity, skip email updates to avoid uniqueness issues, and skip password changes here.
        
        User saved = userRepository.save(user);
        saved.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", saved));
    }

    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Long userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
            
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Unauthorized", null));
        }

        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "User not found", null));
        }

        // Delete associated assessment results first to avoid foreign key constraints
        assessmentResultRepository.deleteByUserId(userId);
        
        // Delete the user
        userRepository.deleteById(userId);

        return ResponseEntity.ok(new ApiResponse<>(true, null, "User account permanently deleted"));
    }
}

