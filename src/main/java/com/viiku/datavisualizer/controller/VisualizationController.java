package com.viiku.visualizer.controller;

import com.viiku.datavisualizer.service.DataVisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visualization")
public class VisualizationController {

    private final DataVisualizationService dataVisualizationService; // Inject your service

    @GetMapping("/visualize/{processingId}")
    public ResponseEntity<List<Map<String, Object>>> getVisualizationData(@PathVariable String processingId) {
        // This method assumes data has already been configured and processed for visualization
        List<Map<String, Object>> visualizationData = dataVisualizationService.getPreparedData(processingId);
        if (visualizationData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visualizationData);
    }
}
