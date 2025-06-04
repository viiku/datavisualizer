package com.viiku.datavisualizer.controller;

import com.viiku.datavisualizer.common.exception.FileParsingException;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import com.viiku.datavisualizer.service.FileProcessingService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
public class FileUploadController {

    private final FileProcessingService fileProcessingService;

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam(value = "file", required = true) MultipartFile file) {

        try {
            String contentType = file.getContentType();
            if (contentType == null ||
                    !(contentType.equals("text/csv") ||
                            contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                            contentType.equals("application/pdf"))) {

                UUID randUUID = UUID.randomUUID();
                return ResponseEntity
                        .badRequest()
                        .body(FileUploadResponse.builder()
                        .fileId(randUUID)
                        .message("Unsupported file type. Please upload CSV, Excel, or PDF.")
                        .status(FileUploadStatus.FAILED)
                        .build());
            }

            FileUploadResponse fileUploadResponse = fileProcessingService.processFileAsync(file); // Process asynchronously
            return ResponseEntity.accepted().body(fileUploadResponse);
        } catch (FileParsingException e) {
            return ResponseEntity.badRequest().body(
                    FileUploadResponse.builder()
                            .status(FileUploadStatus.FAILED)
                            .message("File parsing failed: " + e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FileUploadResponse.builder()
                            .status(FileUploadStatus.FAILED)
                            .message("Internal server error: " + e.getMessage())
                            .build()
            );
        }
    }

//    @GetMapping("/{fileId}/records")
//    public ResponseEntity<?> getRecordsById(@PathVariable String fileId) {
//        // TODO: Implement retrieval of parsed JSON or data preview
//        return ResponseEntity.ok(Map.of("message", "Data retrieval not implemented yet", "fileId", fileId));
//    }
//
//    @GetMapping("/{uploadId}/status")
//    public ResponseEntity<FileStatusResponse> getFileStatus(@PathVariable String uploadId) {
//        FileStatusResponse fileStatusResponse = fileUploadService.getFileStatus(uploadId);
//        return ResponseEntity.ok(fileStatusResponse);
//    }
}