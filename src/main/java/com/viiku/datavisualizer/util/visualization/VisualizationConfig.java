package com.viiku.datavisualizer.util.visualization;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type", // This comes from the outer class
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MapVisualizationConfig.class, name = "MAP"),
        @JsonSubTypes.Type(value = BarChartVisualizationConfig.class, name = "BAR"),
        @JsonSubTypes.Type(value = LineChartVisualizationConfig.class, name = "LINE")
})
public interface VisualizationConfig {
}
