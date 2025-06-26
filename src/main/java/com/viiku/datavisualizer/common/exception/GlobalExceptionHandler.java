//package com.viiku.datavisualizer.common.exception;
//
//import com.viiku.datavisualizer.model.dtos.payload.response.FileStatusResponse;
//import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
//import com.viiku.datavisualizer.model.enums.FileUploadStatus;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.UUID;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(FileUploadException.class)
//    public ResponseEntity<FileUploadResponse> handleFileUploadException(FileUploadException ex) {
//        log.error("File upload failed", ex);
//        return ResponseEntity.badRequest().body(
//                FileUploadResponse.builder()
//                        .fileId(null)
//                        .fileName(null)
//                        .fileSize(null)
//                        .fileType(null)
//                        .uploadTimestamp(null)
//                        .uploadStatus(FileUploadStatus.FAILED)
//                        .message(ex.getMessage())
//                        .detectedMetrics(null)
//                        .build()
//        );
//    }
//
//    @ExceptionHandler(FileParsingException.class)
//    public ResponseEntity<FileStatusResponse> handleFileParsingException(FileParsingException ex) {
//        log.error("File parsing failed", ex);
//        return ResponseEntity.badRequest().body(
//                FileStatusResponse.builder()
//                        .fileId(fileId)
//                        .fileUploadStatus(entity.getStatus())
//                        .progress(10)
//                        .processedRecords(1000)
//                        .totalRecords(12000)
//                        .message("Getting entity from DB")
//                        .build()
//        );
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleGenericException(Exception ex) {
//        log.error("Unexpected server error", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                new ErrorResponse("Internal Server Error", ex.getMessage())
//        );
//    }
//
//    // Optional: simple DTO for generic errors
//    record ErrorResponse(String error, String details) {}
//}
