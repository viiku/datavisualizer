package com.viiku.datavisualizer.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API Response wrapper
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private String error;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String error, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String error, String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
