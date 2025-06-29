package com.viiku.datavisualizer.model.dtos;

import com.viiku.datavisualizer.model.enums.files.ColumnDataType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectedSchemaDto {

    private String columnName;
    private ColumnDataType columnDataType;
    private String geographicalLevel;
    private List<String> sampleValues;
    private int uniqueValues;
}
