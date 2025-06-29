package com.viiku.datavisualizer.model.payload.response;


import com.viiku.datavisualizer.model.enums.visualizer.VisualizationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationResponse {
    private UUID visualizationId;
    private String name;
    private VisualizationStatus status;
    private String shareUrl;
    private String embedCode;
}
