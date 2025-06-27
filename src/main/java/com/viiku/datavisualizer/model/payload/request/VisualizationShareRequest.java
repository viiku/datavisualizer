package com.viiku.datavisualizer.model.payload.request;

import com.viiku.datavisualizer.model.enums.VisualizationSharePermission;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationShareRequest {

    private int expiryDays;
    private VisualizationSharePermission sharePermission;
    private String password;
}
