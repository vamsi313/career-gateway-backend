package com.example.careergateway.dto;

public class AssessmentSaveRequest {
    private Long userId;
    private String assessmentType;
    private String answersJson;

    public AssessmentSaveRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }

    public String getAnswersJson() { return answersJson; }
    public void setAnswersJson(String answersJson) { this.answersJson = answersJson; }
}
