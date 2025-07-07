package com.viiku.datavisualizer.service;

import com.viiku.datavisualizer.model.payload.request.VisualizationExportRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface VisualizationService {

    VisualizationResponse createVisualization(VisualizationRequest request);

    VisualizationResponse updateVisualization(UUID vizId, VisualizationRequest request);

    VisualizationResponse getVisualizationData(UUID vizId);

    VisualizationResponse exportVisualization(UUID vizId, VisualizationExportRequest request);

}
