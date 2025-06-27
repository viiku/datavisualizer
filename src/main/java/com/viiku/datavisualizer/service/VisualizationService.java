package com.viiku.datavisualizer.service;

import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;

public interface VisualizationService {

    VisualizationResponse createVisualization(VisualizationRequest request);
}
