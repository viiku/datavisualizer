package com.viiku.datavisualizer.controller;

import com.viiku.datavisualizer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StatusController {

    private final FileService fileService; // Inject your service

//    @GetMapping("/status/{processingId}")
//    public ResponseEntity<Map<String, Object>> getProcessingStatus(@PathVariable String processingId) {
//        Map<String, Object> status = fileService.getProcessingStatus(processingId);
//        if (status.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(status);
//    }
}
