package com.viiku.datavisualizer.util.visualization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarChartVisualizationConfig implements VisualizationConfig {

    private String xColumn;
    private String yColumn;
    private String barColor;
    private boolean stacked;
}
