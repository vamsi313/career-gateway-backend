package com.example.careergateway.controller;

import com.example.careergateway.dto.ApiResponse;
import com.example.careergateway.dto.AssessmentSaveRequest;
import com.example.careergateway.entity.AssessmentResult;
import com.example.careergateway.repository.AssessmentResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    private AssessmentResultRepository assessmentRepository;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AssessmentResult>> saveAssessment(@RequestBody AssessmentSaveRequest request) {
        logger.info("Saving assessment for user ID: {}, type: {}", request.getUserId(), request.getAssessmentType());

        AssessmentResult result = AssessmentResult.builder()
                .userId(request.getUserId())
                .assessmentType(request.getAssessmentType())
                .answersJson(request.getAnswersJson())
                .build();
        
        AssessmentResult saved = assessmentRepository.save(result);
        logger.info("Assessment saved successfully with ID: {}", saved.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, null, saved));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<List<AssessmentResult>>> getHistory(@PathVariable Long userId) {
        logger.info("Fetching assessment history for user ID: {}", userId);
        List<AssessmentResult> results = assessmentRepository.findByUserId(userId);
        logger.info("Found {} assessment results for user ID: {}", results.size(), userId);
        return ResponseEntity.ok(new ApiResponse<>(true, null, results));
    }
}
