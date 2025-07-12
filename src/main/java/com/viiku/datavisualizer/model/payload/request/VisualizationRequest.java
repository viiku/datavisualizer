package com.viiku.datavisualizer.model.payload.request;

import com.viiku.datavisualizer.model.enums.visualizer.VisualizationType;
import com.viiku.datavisualizer.model.enums.visualizer.VizType;
import com.viiku.datavisualizer.util.visualization.VisualizationConfig;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationRequest {

    @NotNull
    private UUID vizId;

    @Enumerated(EnumType.STRING)
    private VizType vizType;

    private VisualizationConfig configuration;
}
