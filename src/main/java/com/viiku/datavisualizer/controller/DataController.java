package com.viiku.datavisualizer.controller;

import com.viiku.datavisualizer.model.payload.request.DataSchemaRequest;
import com.viiku.datavisualizer.model.payload.response.DataMappingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.viiku.datavisualizer.model.payload.request.DataMappingRequest;

import java.util.UUID;

/**
 * Data Mapping & Configuration APIs
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/data")
public class DataController {

    /**
     * Smart File mapping and Auto-Detection
     * @param datasetId takes datasetId
     * @param request request options
     * @return null
     */
    @PostMapping("/{datasetId}/mapping")
    public ResponseEntity<DataMappingResponse> getDataSet(
            @Valid @PathVariable UUID datasetId,
            @RequestBody DataMappingRequest request) {
        return null;
    }

    /**
     * Column Mapping & Schema Refinement
     * @param datasetId takes datasetId as input
     * @param request takes DataSchemaRequest
     * @return null
     */
    @PutMapping("/{datasetId}/schema")
    public ResponseEntity<?> getDatasetSchema(@Valid @PathVariable UUID datasetId, @RequestBody DataSchemaRequest request) {
        return null;
    }

    /**
     * Get Processed Data
     */
    @GetMapping("/{datasetId}")
    public ResponseEntity<?> getProcessedData(@Valid @PathVariable UUID datasetId, @RequestBody DataSchemaRequest request) {
        return null;
    }
}
