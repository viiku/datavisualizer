package com.viiku.datavisualizer.controller;

import com.viiku.datavisualizer.common.exception.InvalidFileTypeException;
import com.viiku.datavisualizer.model.payload.response.ApiResponse;
import com.viiku.datavisualizer.model.payload.response.FileListResponse;
import com.viiku.datavisualizer.model.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.enums.files.FileType;
import com.viiku.datavisualizer.model.enums.files.FileStatus;
import com.viiku.datavisualizer.service.FileService;
import com.viiku.datavisualizer.util.FileValidation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * File Management APIs
 * Handles file upload, processing status, listing, and deletion
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;
    private final FileValidation fileValidation;

    /**
     * Upload and process data file
     * Supports CSV, Excel (XLS/XLSX), and PDF files
     */
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestParam(value = "file") MultipartFile file) {

        log.info("File upload requested: {} ({})",
                file.getOriginalFilename(), formatFileSize(file.getSize()));

        try {
            fileValidation.validateUploadedFile(file);

            FileUploadResponse response = fileService.uploadAndProcessFile(file);

            log.info("File upload successful: fileId={}, status={}",
                    response.getFileId(), response.getUploadStatus());

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(ApiResponse.success(response, "File uploaded successfully and processing started"));

        } catch (InvalidFileTypeException e) {
            log.warn("Invalid file type uploaded: {}", e.getMessage());
            return createErrorResponse(file, HttpStatus.BAD_REQUEST,
                    "Invalid file type", e.getMessage());
//        } catch (FileParsingException e) {
//            log.error("File parsing failed for {}: {}", file.getOriginalFilename(), e.getMessage());
//            return createErrorResponse(file, HttpStatus.BAD_REQUEST,
//                    "File parsing failed", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during file upload: {}", e.getMessage(), e);
            return createErrorResponse(file, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal server error", "An unexpected error occurred while processing the file");
        }
    }

    /**
     * Get file processing status and metadata
     */
    @GetMapping("/{fileId}/status")
    public ResponseEntity<ApiResponse<FileStatusResponse>> getFileStatus(@PathVariable UUID fileId) {
        log.debug("Getting file status for fileId: {}", fileId);

        try {
            FileStatusResponse statusResponse = fileService.getFileStatus(fileId);
            return ResponseEntity.ok(ApiResponse.success(statusResponse, "File Status Retrieved"));

//        } catch (FileNotFoundException e) {
//            log.warn("File not found: {}", fileId);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error("File Not Found", e.getMessage()));

        } catch (Exception e) {
            log.error("Error getting file status for {}: {}", fileId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error", "Failed to retrieve file status"));
        }
    }

    /**
     * Get List of uploaded files
     */
    @GetMapping
    public ResponseEntity<ApiResponse<FileListResponse>> getFileList(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
        @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
        @RequestParam(value = "sortBy", defaultValue = "uploadTimestamp") String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
        @RequestParam(value = "status", required = false) FileStatus status,
        @RequestParam(value = "fileType", required = false) FileType fileType) {

            log.debug("Getting file list: page={}, size={}, sortBy={}, sortDir={}",
                    page, size, sortBy, sortDir);

            try {
                // Create pageable with sorting
                Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ?
                        Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

                FileListResponse fileList = fileService.getFileList(pageable, status, fileType);

                return ResponseEntity.ok(ApiResponse.success(fileList, "File list retrieved"));

            } catch (Exception e) {
                log.error("Error getting file list: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Internal server error", "Failed to retrieve file list"));
            }
    }

    /**
     * Delete file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(UUID fileId) {

        log.info("Delete request for fileId: {}", fileId);

        try {
            fileService.deleteFile(fileId);
            log.info("File deleted successfully: {}", fileId);

            return ResponseEntity.ok(ApiResponse.success(null, "File deleted successfully"));

//        } catch (FileNotFoundException e) {
//            log.warn("Attempted to delete non-existent file: {}", fileId);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error("File not found", e.getMessage()));
//
        } catch (Exception e) {
            log.error("Error deleting file {}: {}", fileId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error", "Failed to delete file"));
        }
    }


    /**
     * Get file preview data (first N rows)
     */
    @GetMapping("/{fileId}/preview")
    public ResponseEntity<ApiResponse<Object>> getFilePreview(
            @PathVariable UUID fileId,
            @RequestParam(value = "rows", defaultValue = "10") @Min(1) @Max(100) int rows) {

        log.debug("Getting preview for fileId: {} (rows={})", fileId, rows);

        try {
            Object previewData = fileService.getFilePreview(fileId, rows);
            return ResponseEntity.ok(ApiResponse.success(previewData, "File preview retrieved"));

//        } catch (FileNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error("File not found", e.getMessage()));

        } catch (Exception e) {
            log.error("Error getting file preview for {}: {}", fileId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error", "Failed to retrieve file preview"));
        }
    }

    /**
     * Retry file processing
     */
    @PostMapping("/{fileId}/retry")
    public ResponseEntity<ApiResponse<FileUploadResponse>> retryFileProcessing(@PathVariable UUID fileId) {
        log.info("Retry processing request for fileId: {}", fileId);

        try {
            FileUploadResponse response = fileService.retryProcessing(fileId);
            return ResponseEntity.ok(ApiResponse.success(response, "File processing restarted"));

//        } catch (FileNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error("File not found", e.getMessage()));

        } catch (Exception e) {
            log.error("Error retrying file processing for {}: {}", fileId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error", "Failed to retry file processing"));
        }
    }

    // Helper methods

    /**
     * Create standardized error response for file upload failures
     */
    private ResponseEntity<ApiResponse<FileUploadResponse>> createErrorResponse(
            MultipartFile file, HttpStatus status, String error, String message) {

        FileUploadResponse errorResponse = FileUploadResponse.builder()
                .fileId(null)
                .fileName(Optional.ofNullable(file.getOriginalFilename()).orElse("unknown"))
                .fileSize(file.getSize())
                .fileType(FileType.UNKNOWN)
                .uploadTimestamp(LocalDateTime.now())
                .uploadStatus(FileStatus.FAILED)
//                .message(message)
                .detectedMetrics(null)
                .build();

        return ResponseEntity.status(status)
                .body(ApiResponse.error(error, message, errorResponse));
    }

    /**
     * Format file size for logging
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}