package com.viiku.datavisualizer.util.visualization;

import com.viiku.datavisualizer.model.enums.visualizer.MapStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapSetting {

    private MapCenter mapCenter;
    private int zoom;
    private MapStyle style;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MapCenter {
        private double latitude;
        private double longitude;
    }
}
