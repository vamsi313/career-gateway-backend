package com.example.careergateway.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_results")
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "assessment_type", nullable = false)
    private String assessmentType; // "personality", "skills", "interest"

    @Column(name = "answers_json", columnDefinition = "TEXT", nullable = false)
    private String answersJson; // Storing answers as JSON string

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        completedAt = LocalDateTime.now();
    }

    public AssessmentResult() {}

    public AssessmentResult(Long id, Long userId, String assessmentType, String answersJson, LocalDateTime completedAt) {
        this.id = id;
        this.userId = userId;
        this.assessmentType = assessmentType;
        this.answersJson = answersJson;
        this.completedAt = completedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }

    public String getAnswersJson() { return answersJson; }
    public void setAnswersJson(String answersJson) { this.answersJson = answersJson; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    @Override
    public String toString() {
        return "AssessmentResult{" +
                "id=" + id +
                ", userId=" + userId +
                ", assessmentType='" + assessmentType + '\'' +
                ", completedAt=" + completedAt +
                '}';
    }

    // Builder pattern
    public static AssessmentResultBuilder builder() { return new AssessmentResultBuilder(); }

    public static class AssessmentResultBuilder {
        private Long userId;
        private String assessmentType;
        private String answersJson;

        public AssessmentResultBuilder userId(Long userId) { this.userId = userId; return this; }
        public AssessmentResultBuilder assessmentType(String assessmentType) { this.assessmentType = assessmentType; return this; }
        public AssessmentResultBuilder answersJson(String answersJson) { this.answersJson = answersJson; return this; }

        public AssessmentResult build() {
            AssessmentResult result = new AssessmentResult();
            result.userId = this.userId;
            result.assessmentType = this.assessmentType;
            result.answersJson = this.answersJson;
            return result;
        }
    }
}
