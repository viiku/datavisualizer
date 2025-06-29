package com.viiku.datavisualizer.model.dtos;

import com.viiku.datavisualizer.model.enums.files.ColumnDataType;
import com.viiku.datavisualizer.model.enums.files.ColumnRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {

    private String name;
    private ColumnDataType dataType;
    private boolean nullable;
    private Object sampleValue;
    private ColumnRole columnRole;
}
