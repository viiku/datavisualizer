package com.viiku.visualizer.controller;

import com.viiku.visualizer.common.exception.FileParsingException;
import com.viiku.visualizer.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String jsonData = fileService.parseFile(file);
            return ResponseEntity.ok(jsonData);
        } catch (FileParsingException e) {
            return ResponseEntity.badRequest().body("File parsing failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

}