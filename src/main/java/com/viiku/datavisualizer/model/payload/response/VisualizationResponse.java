package com.viiku.datavisualizer.model.payload.response;


import com.viiku.datavisualizer.model.enums.visualizer.VisualizationExportFormat;
import com.viiku.datavisualizer.model.enums.visualizer.VisualizationStatus;
import com.viiku.datavisualizer.model.enums.visualizer.VisualizationType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationResponse {
    private UUID visualizationId;
    private VisualizationType type;
    private VisualizationStatus status;
    private String shareUrl;
    private String embedUrl;
    private VisualizationExportFormat exportFormat;
}
