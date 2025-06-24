package com.inventory.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private Boolean status;
    private String message;
    private int errorCode;
    private Instant timestamp;
    private T data;

    public ApiResponse(Boolean status, String message, int errorCode, T data) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, 0, Instant.now(), data);
    }

    public static <T> ApiResponse<T> failure(String message, int errorCode) {
        return new ApiResponse<>(false, message, errorCode, Instant.now(), null);
    }
}