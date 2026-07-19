package com._xibrahim.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiResponse", description = "Unified API response envelope used for successful and failed requests.")
public class ResponseDto<T> {

    @Schema(description = "Indicates whether the request completed successfully.", example = "true")
    private boolean success;

    @Schema(description = "Application status code matching the HTTP response status.", example = "200")
    private String statusCode;

    @Schema(description = "Human-readable response message.", example = "Request processed successfully")
    private String statusMsg;

    @Schema(description = "Response payload. Its shape depends on the endpoint.")
    private T data;

    @Schema(description = "Time when the response was created.", example = "2026-07-19T15:30:00")
    private LocalDateTime timestamp;

    public static <T> ResponseDto<T> success(String statusCode, String statusMsg, T data) {
        return new ResponseDto<>(true, statusCode, statusMsg, data, LocalDateTime.now());
    }

    public static <T> ResponseDto<T> failure(String statusCode, String statusMsg, T data) {
        return new ResponseDto<>(false, statusCode, statusMsg, data, LocalDateTime.now());
    }
}
