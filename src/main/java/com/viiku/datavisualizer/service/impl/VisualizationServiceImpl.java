package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;
import com.viiku.datavisualizer.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisualizationServiceImpl implements VisualizationService {

    @Override
    public VisualizationResponse createVisualization(VisualizationRequest request) {
        return null;
    }
}
