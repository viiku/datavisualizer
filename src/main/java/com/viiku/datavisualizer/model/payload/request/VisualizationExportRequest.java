package com.viiku.datavisualizer.model.payload.request;

import com.viiku.datavisualizer.model.enums.visualizer.VisualizationExportFormat;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationExportRequest {

    private VisualizationExportFormat exportFormat;
    private int width;
    private int height;
    private int dpi;
    private boolean includeData;
}
