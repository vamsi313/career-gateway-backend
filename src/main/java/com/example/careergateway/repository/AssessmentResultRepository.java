package com.example.careergateway.repository;

import com.example.careergateway.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByUserId(Long userId);
    List<AssessmentResult> findByUserIdAndAssessmentType(Long userId, String assessmentType);
    
    @org.springframework.transaction.annotation.Transactional
    void deleteByUserId(Long userId);
}
