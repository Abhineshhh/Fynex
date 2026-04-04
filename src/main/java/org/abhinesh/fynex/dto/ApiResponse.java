package org.abhinesh.fynex.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Success response with data
    public static <T> ApiResponse<T> success(String msg, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(msg)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Success response without data
    public static <T> ApiResponse<T> success(String msg){
        return ApiResponse.<T>builder()
                .success(true)
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Error response
    public static <T> ApiResponse<T> error(String msg){
        return ApiResponse.<T>builder()
                .success(false)
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
