package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.model.enums.visualizer.VisualizationStatus;
import com.viiku.datavisualizer.model.enums.visualizer.VisualizationType;
import com.viiku.datavisualizer.model.payload.request.VisualizationExportRequest;
import com.viiku.datavisualizer.model.payload.request.VisualizationRequest;
import com.viiku.datavisualizer.model.payload.response.VisualizationResponse;
import com.viiku.datavisualizer.service.VisualizationService;
import com.viiku.datavisualizer.util.visualization.MapVisualizationConfig;
import com.viiku.datavisualizer.util.visualization.VisualizationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.viiku.datavisualizer.model.enums.visualizer.VizType.*;

@Service
@RequiredArgsConstructor
public class VisualizationServiceImpl implements VisualizationService {

    @Override
    public VisualizationResponse createVisualization(VisualizationRequest request) {


        VisualizationType type;

        // Correct enum comparison using equals() or switch
        switch (request.getVizType()) {
            case MAP -> type = VisualizationType.CHOROPLETH;
            case BAR -> type = VisualizationType.BAR; // You may define this
            case CHART -> type = VisualizationType.LINE; // Optional: adjust if needed
            default -> throw new IllegalArgumentException("Unsupported vizType: " + request.getVizType());
        }

        VisualizationStatus status = VisualizationStatus.CREATED;

        // Optional: type-check config if needed
        VisualizationConfig config = request.getConfiguration();

        // Log or validate config type
        if (type == VisualizationType.CHOROPLETH && !(config instanceof MapVisualizationConfig)) {
            throw new IllegalArgumentException("Expected MapVisualizationConfig for CHOROPLETH");
        }

        // TODO: Save entity to DB if applicable

        return VisualizationResponse.builder()
                .vizId(request.getVizId())
                .type(type)
                .status(status)
                .config(config)
                .build();
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
