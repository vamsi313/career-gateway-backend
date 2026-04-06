package com.example.careergateway.controller;

import com.example.careergateway.dto.ApiResponse;
import com.example.careergateway.dto.AssessmentSaveRequest;
import com.example.careergateway.entity.AssessmentResult;
import com.example.careergateway.repository.AssessmentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentResultRepository assessmentRepository;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AssessmentResult>> saveAssessment(@RequestBody AssessmentSaveRequest request) {
        AssessmentResult result = AssessmentResult.builder()
                .userId(request.getUserId())
                .assessmentType(request.getAssessmentType())
                .answersJson(request.getAnswersJson())
                .build();
        
        AssessmentResult saved = assessmentRepository.save(result);
        return ResponseEntity.ok(new ApiResponse<>(true, null, saved));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<List<AssessmentResult>>> getHistory(@PathVariable Long userId) {
        List<AssessmentResult> results = assessmentRepository.findByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, null, results));
    }
}
