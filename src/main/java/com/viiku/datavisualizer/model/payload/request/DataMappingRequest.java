package com.viiku.datavisualizer.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMappingRequest {

    private boolean autoDetectColumns;
    private int skipRows;
    private String encoding;
    private String locale;
}
