package com.example.careergateway.controller;

import com.example.careergateway.dto.ApiResponse;
import com.example.careergateway.entity.User;
import com.example.careergateway.repository.UserRepository;
import com.example.careergateway.repository.AssessmentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentResultRepository assessmentRepository;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Remove passwords before sending
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(new ApiResponse<>(true, null, users));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        long totalUsers = userRepository.count();
        long totalAssessments = assessmentRepository.count();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalAssessments", totalAssessments);
        
        return ResponseEntity.ok(new ApiResponse<>(true, null, stats));
    }
}
