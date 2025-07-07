package com.viiku.datavisualizer.controller;

import com.viiku.datavisualizer.model.payload.request.VisualizationExportRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationShareRequest;
import com.viiku.datavisualizer.model.payload.response.ApiResponse;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;
import com.viiku.datavisualizer.service.VisualizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    /**
     *
     * @param visualizationRequest basically will have datasetId
     * @return json data for rendering
     */
    @PostMapping
    public ResponseEntity<ApiResponse<VisualizationResponse>> createVisualization(
            @Valid @RequestBody VisualizationRequest visualizationRequest) {

        VisualizationResponse response = visualizationService.createVisualization(visualizationRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(response, "Visualization Image created successfully and processing started"));
    }

    /**
     *
     * @param vizId get visualizer Id configuration
     * @return null as of now
     */
    @GetMapping("/{vizId}")
    public ResponseEntity<ApiResponse<VisualizationResponse>> getVisualizationConfiguration(
            @Valid @PathVariable UUID vizId) {
        return null;
    }

    /**
     *
     * @param vizId update visualizer Id configuration
     * @return null as of now
     */
    @PutMapping("/{vizId}")
    public ResponseEntity<ApiResponse<VisualizationResponse>> updateVisualization(
            @PathVariable UUID vizId,
            @Valid @RequestBody VisualizationRequest updateRequest) {

        VisualizationResponse response = visualizationService.updateVisualization(vizId, updateRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Visualization updated successfully"));
    }

    @GetMapping("/{vizId}/data")
    public ResponseEntity<ApiResponse<VisualizationResponse>> getVisualizationData(@PathVariable UUID vizId) {
        VisualizationResponse response = visualizationService.getVisualizationData(vizId);
        return ResponseEntity.ok(ApiResponse.success(response, "Visualization data retrieved successfully"));
    }

    /**
     *
     * Get Visualization Analytics
     */
    @GetMapping("/{vizId}/analytics")
    public ResponseEntity<ApiResponse<VisualizationResponse>> getAnalytics(@Valid @PathVariable UUID vizId) {
        return null;
    }

    /**
     *
     * Export Visualization
     */
    @PostMapping("/{vizId}/export")
    public ResponseEntity<ApiResponse<VisualizationResponse>> exportVisualization(
            @PathVariable UUID vizId,
            @Valid @RequestBody VisualizationExportRequest exportRequest) {

        VisualizationResponse exportUrl = visualizationService.exportVisualization(vizId, exportRequest);
        return ResponseEntity.ok(ApiResponse.success(exportUrl, "Export initiated successfully"));
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
