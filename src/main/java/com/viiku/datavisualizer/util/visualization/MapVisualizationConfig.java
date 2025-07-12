package com.viiku.datavisualizer.util.visualization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapVisualizationConfig implements VisualizationConfig {

    private String colorColumn;
    private String colorScheme;
    private String colorScale;
    private List<String> tooltipColumns;
    private float opacity;
    private String legendTitle;
    private MapSetting mapSettings;
}
