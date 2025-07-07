package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.model.payload.request.VisualizationExportRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;
import com.viiku.datavisualizer.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisualizationServiceImpl implements VisualizationService {

    @Override
    public VisualizationResponse createVisualization(VisualizationRequest request) {
        return null;
    }

    @Override
    public VisualizationResponse updateVisualization(UUID vizId, VisualizationRequest request) {
        return null;
    }

    @Override
    public VisualizationResponse getVisualizationData(UUID vizId) {
        return null;
    }

    @Override
    public VisualizationResponse exportVisualization(UUID vizId, VisualizationExportRequest request) {
        return null;
    }
}
