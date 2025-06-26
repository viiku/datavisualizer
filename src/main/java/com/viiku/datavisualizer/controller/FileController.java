package com.viiku.datavisualizer.controller;

import com.viiku.datavisualizer.common.exception.FileParsingException;
import com.viiku.datavisualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.enums.FileType;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import com.viiku.datavisualizer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static com.viiku.datavisualizer.util.FileConstants.*;


/**
 * File Management APIs
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam(value = "file") MultipartFile file) {

        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
        Long fileSize = file.getSize();

        try {
            FileUploadResponse response = fileService.uploadFile(file);
            return ResponseEntity.accepted().body(response);

        } catch (FileParsingException e) {
            return ResponseEntity.badRequest().body(
                    FileUploadResponse.builder()
                            .fileId(null)
                            .fileName(fileName)
                            .fileSize(fileSize)
                            .fileType(FileType.UNKNOWN)
                            .uploadTimestamp(null)
                            .uploadStatus(FileUploadStatus.FAILED)
                            .message("File parsing failed: " + e.getMessage())
                            .detectedMetrics(null)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FileUploadResponse.builder()
                            .fileId(null)
                            .fileName(fileName)
                            .fileSize(fileSize)
                            .fileType(FileType.UNKNOWN)
                            .uploadTimestamp(null)
                            .uploadStatus(FileUploadStatus.FAILED)
                            .message("Internal server error: " + e.getMessage())
                            .detectedMetrics(null)
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

    /**
     *
     * @return File Status response
     *
     */
    @GetMapping("/{fileId}/status")
    public ResponseEntity<FileStatusResponse> getFileStatus(@PathVariable UUID fileId) {
        FileStatusResponse fileStatusResponse = fileService.getFileStatus(fileId);
        return ResponseEntity.ok(fileStatusResponse);
    }
}