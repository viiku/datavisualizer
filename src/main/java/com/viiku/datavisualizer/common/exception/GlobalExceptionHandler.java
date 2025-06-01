package com.viiku.datavisualizer.common.exception;

import com.viiku.datavisualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FileUploadResponse> handleGenericException(Exception ex) {
        logger.error("Caught generic exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                FileUploadResponse.builder()
                        .status(FileUploadStatus.FAILED)
                        .message("Unexpected error: " + ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<FileUploadResponse> handleFileUploadException(FileUploadException ex) {
        logger.error("Caught file upload exception", ex);
        return ResponseEntity.badRequest().body(
                FileUploadResponse.builder()
                        .status(FileUploadStatus.FAILED)
                        .message(ex.getMessage())
                        .build()
        );
    }

//    @ExceptionHandler(FileParsingException.class)
//    public ResponseEntity<FileStatusResponse> handleFileParsingException(FileParsingException ex) {
//        logger.error("Caught file parsing exception");
//        return ResponseEntity.badRequest().body(
//                FileStatusResponse.builder()
//                        .status(FileUploadStatus.FAILED)
//                        .message(ex.getMessage())
//                        .build()
//        );
//    }
}
