package com.viiku.visualizer.controller;

import com.viiku.datavisualizer.model.payload.request.VisualizationExportRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationShareRequest;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;
import com.viiku.datavisualizer.service.VisualizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 *
 * Visualization Configuration APIs
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visualizations")
public class VisualizerController {

    private final VisualizationService visualizationService;

    @PostMapping
    public ResponseEntity<VisualizationResponse> createVisualization(@Valid @RequestBody VisualizationRequest visualizationRequest) {

        VisualizationResponse response = visualizationService.createVisualization(visualizationRequest);
        return ResponseEntity.accepted().body(response);
    }

    @PutMapping("/{vizId}")
    public String updateVisualization(@Valid @PathVariable UUID vizId) {
        return "null";
    }

    @GetMapping("/{vizId}/data")
    public String getVisualizationData(@Valid @PathVariable UUID vizId) {
        return null;
    }

    /**
     *
     * Get Visualization Analytics
     */
    @GetMapping("/{vizId}/analytics")
    public String getAnalytics(@Valid @PathVariable UUID vizId) {
        return null;
    }

    /**
     *
     * Export Visualization
     */
    @PostMapping("/{vizId}/export")
    public ResponseEntity<?> exportVisualization(@Valid @PathVariable UUID vizId,
                                                      @RequestBody VisualizationExportRequest exportRequest) {
        return null;
    }

    /**
     *
     * Generate Share Link
     */
    @PostMapping("/{vizId}/share")
    public ResponseEntity<?> shareVisualization(@Valid @PathVariable UUID vizId,
                                                      @RequestBody VisualizationShareRequest shareRequest) {
        return null;
    }
}
