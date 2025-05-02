package com.viiku.visualizer.model.dtos.payload.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisualizationRequest {

    private String fieldId;
    private String metric;
    private String theme;
}
