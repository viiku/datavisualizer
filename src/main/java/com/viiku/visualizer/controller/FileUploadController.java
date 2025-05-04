package com.viiku.visualizer.controller;

import com.viiku.visualizer.common.exception.FileParsingException;
import com.viiku.visualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.visualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.visualizer.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadResponse response = fileUploadService.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (FileParsingException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "File parsing failed", "message", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Internal Server Error", "message", e.getMessage())
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