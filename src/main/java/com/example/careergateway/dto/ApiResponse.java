package com.example.careergateway.dto;

public class ApiResponse<T> {
    private boolean success;
    private String error;
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean success, String error, T data) {
        this.success = success;
        this.error = error;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
