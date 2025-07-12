package com.viiku.datavisualizer.util.visualization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineChartVisualizationConfig implements VisualizationConfig {

    private String xColumn;
    private String yColumn;
    private String lineColor;
    private boolean stacked;
}
