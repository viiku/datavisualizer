package com.viiku.datavisualizer.model.payload.request;

import com.viiku.datavisualizer.model.enums.visualizer.MapStyle;
import com.viiku.datavisualizer.model.enums.visualizer.VisualizationType;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationRequest {

    private String name;
    private String description;
    private VisualizationType type;
    private VisualizationConfiguration configuration;
    private MapSettings mapSettings;

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class VisualizationConfiguration {
        private String colorColumn;
        private String colorScheme;
        private String colorScale;
        private String sizeColumn;
        private List<String> tooltipColumns;
        private String legendTitle;
        private float opacity;
        private String borderColor;
        private int borderWidth;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MapSettings {
        private MapCenter mapCenter;
        private int zoom;
        private MapStyle style;
    }

    private static class MapCenter {
        private double lat;
        private double lng;
    }
}
